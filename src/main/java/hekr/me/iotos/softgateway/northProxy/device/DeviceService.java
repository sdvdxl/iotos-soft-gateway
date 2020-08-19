package hekr.me.iotos.softgateway.northProxy.device;

import static java.util.stream.Collectors.toList;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.EnergyStatDataReq;
import hekr.me.iotos.softgateway.common.dto.EnergyStatDataResp;
import hekr.me.iotos.softgateway.common.dto.RuntimeReq;
import hekr.me.iotos.softgateway.common.dto.RuntimeResp;
import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.Dev;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.common.klink.DevUpgrade;
import hekr.me.iotos.softgateway.common.klink.GetConfigResp;
import hekr.me.iotos.softgateway.common.klink.GetTopoResp;
import hekr.me.iotos.softgateway.common.klink.ModelData;
import hekr.me.iotos.softgateway.northProxy.ProxyConnectService;
import hekr.me.iotos.softgateway.northProxy.ProxyService;
import hekr.me.iotos.softgateway.pluginAsClient.http.HttpClient;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author jiatao
 * @date 2020/7/21
 */
@Service
@Slf4j
public class DeviceService {
  @Autowired private ProxyConfig proxyConfig;
  @Autowired private ProxyService proxyService;
  @Autowired private ProxyConnectService proxyConnectService;

  /** key:devId */
  private static final ConcurrentMap<String, Device> IOT_DEVICE_MAP = new ConcurrentHashMap<>();

  /** key:areaNum */
  private static final ConcurrentMap<Integer, Device> SUBSYSTEM_DEVICE_MAP =
      new ConcurrentHashMap<>();

  public Device getByPkAndDevId(String devId) {
    return IOT_DEVICE_MAP.getOrDefault(devId, null);
  }

  public Device getByAreaNum(Integer areaNum) {
    return SUBSYSTEM_DEVICE_MAP.getOrDefault(areaNum, null);
  }

  public synchronized void updateDevices(List<Device> deviceList) {
    // 更新本地缓存
    IOT_DEVICE_MAP.clear();
    SUBSYSTEM_DEVICE_MAP.clear();
    for (Device device : deviceList) {
      IOT_DEVICE_MAP.put(device.getDevId(), device);
      SUBSYSTEM_DEVICE_MAP.put(device.getAreaNum(), device);
    }
    proxyService.getTopo();
  }

  //  @Scheduled(fixedDelay = 60 * 1000)
  @Scheduled(fixedDelay = 10000)
  public void scheduleUpdateDevStatus() {
    if (proxyConnectService.isConnected()) {
      log.info("正在定时更新设备状态");
    } else {
      log.warn("等待mqtt连接，发送 getTopo,同步设备");
      proxyConnectService.connect();
      return;
    }
    proxyService.getConfig();
  }

  /** 主动获取远程配置的回复 */
  public void getConfigResp(GetConfigResp getConfigResp) {
    String config = HttpUtil.get(getConfigResp.getUrl());
    String[] split = config.split("\n");
    List<Device> deviceList = new ArrayList<>();
    for (String s : split) {
      try {
        Device device = JsonUtil.fromJson(s, Device.class);
        deviceList.add(device);
      } catch (Exception e) {
        log.error("读取远程配置出错", e);
      }
    }
    updateDevices(deviceList);
  }

  /** 网关远程配置下发处理 */
  public void getConfigResp(DevUpgrade klink) {
    String config = HttpUtil.get(klink.getUrl());
    String[] split = config.split("\n");
    List<Device> deviceList = new ArrayList<>();
    for (String s : split) {
      try {
        Device device = JsonUtil.fromJson(s, Device.class);
        deviceList.add(device);
      } catch (Exception e) {
        log.error("读取远程配置出错", e);
      }
    }
    updateDevices(deviceList);
  }

  /**
   * 通过 getTopoResp 来同步设备列表
   *
   * @param klink
   */
  public void syncDevices(GetTopoResp klink) {
    List<String> gatewaySubDevs = klink.getSubs().stream().map(Dev::getDevId).collect(toList());
    doSyncDevice(gatewaySubDevs);
  }

  private void doSyncDevice(List<String> gatewaySubDevs) {
    List<String> iotDeviceIdList =
        IOT_DEVICE_MAP.values().stream().map(Device::getDevId).collect(toList());
    // 找出 remoteDevs 中存在，但是 gatewaySubDevs 中不存在的，进行添加拓扑
    List<String> addTopoList = CollectionUtil.subtractToList(iotDeviceIdList, gatewaySubDevs);
    log.info("需要新添加的设备：{}", addTopoList);
    if (!addTopoList.isEmpty()) {
      // 注册
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            if (device == null) {
              log.warn("【进行注册操作】设备不存在，设备ID为：{}", d);
              return;
            }
            try {
              proxyService.register(
                  proxyConfig.getSUB_PK(),
                  device.getDevId(),
                  proxyConfig.getSUB_PRODUCT_SECRET(),
                  device.getDevName());
            } catch (Exception e) {
              log.warn(e.getMessage());
            }
          });
      sleep(5);

      // 添加拓扑
      log.info("添加拓扑关系");
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            if (device == null) {
              log.warn("【进行添加拓扑关系操作】设备不存在，设备ID为：{}", d);
              return;
            }
            proxyService.addDev(proxyConfig.getSUB_PK(), device.getDevId(), null);
          });

      sleep(5);
      // 登录
      log.info("发送登录信息");
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            if (device == null) {
              log.warn("【进行发送登录信息操作】设备不存在，设备ID为：{}", d);
              return;
            }
            proxyService.devLogin(proxyConfig.getSUB_PK(), device.getDevId());
          });
    }

    // 找出gatewaySubDevs中存在，但是remoteDevs中不存在的，进行删除拓扑
    List<String> delTopoList = CollectionUtil.subtractToList(gatewaySubDevs, iotDeviceIdList);
    log.info("需要删除的设备：{}", delTopoList);
    delTopoList.forEach(
        d -> {
          String[] split = d.split("@");
          proxyService.delDev(split[0], split[1]);
        });
  }

  private void sleep(int timeout) {
    try {
      TimeUnit.SECONDS.sleep(timeout);
    } catch (InterruptedException ignored) {
    }
  }
}
