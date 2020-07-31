package hekr.me.iotos.softgateway.northProxy.device;

import static java.util.stream.Collectors.toList;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.RuntimeReq;
import hekr.me.iotos.softgateway.common.dto.RuntimeResp;
import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.common.klink.DevUpgrade;
import hekr.me.iotos.softgateway.common.klink.GetConfigResp;
import hekr.me.iotos.softgateway.common.klink.GetTopoResp;
import hekr.me.iotos.softgateway.common.klink.ModelData;
import hekr.me.iotos.softgateway.northProxy.ProxyConnectService;
import hekr.me.iotos.softgateway.northProxy.ProxyService;
import hekr.me.iotos.softgateway.pluginAsClient.http.HttpClient;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import java.util.ArrayList;
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
  @Autowired private ProxyService proxyService;
  @Autowired private ProxyConnectService proxyConnectService;

  @Autowired private HttpClient httpClient;

  /** key:pk@devId */
  private static final ConcurrentMap<String, Device> IOT_DEVICE_MAP = new ConcurrentHashMap<>();

  /** key:Id@deviceTypeId */
  private static final ConcurrentMap<String, Device> SUBSYSTEM_DEVICE_MAP =
      new ConcurrentHashMap<>();

  public Device getByPkAndDevId(String pk, String devId) {
    return IOT_DEVICE_MAP.getOrDefault(pk + "@" + devId, null);
  }

  public Device getById(String Id) {
    return SUBSYSTEM_DEVICE_MAP.getOrDefault(Id, null);
  }

  public synchronized void updateDevices(List<Device> deviceList) {
    // 更新本地缓存
    IOT_DEVICE_MAP.clear();
    SUBSYSTEM_DEVICE_MAP.clear();
    for (Device device : deviceList) {
      IOT_DEVICE_MAP.put(device.getPk() + "@" + device.getDevId(), device);
      SUBSYSTEM_DEVICE_MAP.put(device.getDeviceId(), device);
    }
    proxyService.getTopo();
  }

  @Scheduled(fixedDelay = 10 * 1000)
  //  @Scheduled(fixedDelay = 10000)
  public void scheduleUpdateDevStatus() {
    if (proxyConnectService.isConnected()) {
      log.info("正在定时更新设备状态");
    } else {
      log.warn("等待mqtt连接，发送 getTopo,同步设备");
      return;
    }
    proxyService.getConfig();
  }

  public void getConfigResp(GetConfigResp getConfigResp) {
    String config = HttpUtil.get(getConfigResp.getUrl());
    String[] split = config.split("\n");
    List<Device> deviceList = new ArrayList<>();
    for (String s : split) {
      try {
        Device device = JsonUtil.fromJson(s, Device.class);
        deviceList.add(device);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    updateDevices(deviceList);
  }

  public void getConfigResp(DevUpgrade klink) {
    String config = HttpUtil.get(klink.getUrl());
    String[] split = config.split("\n");
    List<Device> deviceList = new ArrayList<>();
    for (String s : split) {
      try {
        Device device = JsonUtil.fromJson(s, Device.class);
        deviceList.add(device);
      } catch (Exception e) {
        e.printStackTrace();
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
    List<String> gatewaySubDevs =
        klink.getSubs().stream().map(dev -> dev.getPk() + "@" + dev.getDevId()).collect(toList());
    doSyncDevice(gatewaySubDevs);
    updateDeviceStatus();
    //    doSyncDeviceInfo(allDevices);
  }

  private void doSyncDevice(List<String> gatewaySubDevs) {
    List<String> iotDeviceIdList =
        IOT_DEVICE_MAP.values().stream()
            .map(device -> device.getPk() + "@" + device.getDevId())
            .collect(toList());
    // 找出 remoteDevs 中存在，但是 gatewaySubDevs 中不存在的，进行添加拓扑
    List<String> addTopoList = CollectionUtil.subtractToList(iotDeviceIdList, gatewaySubDevs);
    log.info("需要新添加的设备：{}", addTopoList);
    if (!addTopoList.isEmpty()) {
      // 注册
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            try {
              proxyService.register(
                  device.getPk(), device.getDevId(), device.getProductSecret(), device.getDevName());
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
      sleep(5);

      // 添加拓扑
      log.info("添加拓扑关系");
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            proxyService.addDev(device.getPk(), device.getDevId(), null);
          });

      sleep(5);
      // 登录
      log.info("发送登录信息");
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            proxyService.devLogin(device.getPk(), device.getDevId());
          });
    }

    // 找出gatewaySubDevs中存在，但是remoteDevs中不存在的，进行删除拓扑
    List<String> delTopoList = CollectionUtil.subtractToList(gatewaySubDevs, iotDeviceIdList);
    log.info("需要删除的设备：{}", delTopoList);
    delTopoList.forEach(
        d -> {
          Device device = IOT_DEVICE_MAP.get(d);
          proxyService.delDev(device.getPk(), device.getDevId());
        });
  }

  /** 更新设备状态 */
  private void updateDeviceStatus() {
    String[] deviceIds = IOT_DEVICE_MAP.values().stream().map(Device::getDeviceId).toArray(String[]::new);
    if (deviceIds.length <= 0) {
      return;
    }
    String deviceIdString = String.join(",", deviceIds);
    RuntimeReq runtimeReq = new RuntimeReq();
    runtimeReq.setDeviceIds(deviceIdString);
    BaseResp<List<RuntimeResp>> runtimeData = httpClient.getRuntimeData(runtimeReq);
    if (runtimeData == null) {
      return;
    }
    List<RuntimeResp> data = runtimeData.getData();
    // 若参数的errorCode为-1则说明设备离线
    List<String> offlineList =
        data.stream()
            .filter(runtimeResp -> runtimeResp.getErrorCode() == -1)
            .map(RuntimeResp::getDeviceId)
            .collect(toList());

    // 对离线设备进行下线操作
    offlineList.forEach(
        s -> {
          Device device = getById(s);
          proxyService.devLogout(device.getPk(), device.getDevId());
        });

    // 对非离线设备进行登录以及数据更新操作
    List<String> onlineList =
        data.stream()
            .filter(runtimeResp -> !offlineList.contains(runtimeResp.getDeviceId()))
            .map(RuntimeResp::getDeviceId)
            .collect(toList());
    onlineList.forEach(
        s -> {
          Device device = getById(s);
          proxyService.devLogin(device.getPk(), device.getDevId());
          DevSend devSend = new DevSend();
          devSend.setPk(device.getPk());
          devSend.setDevId(device.getDevId());
          devSend.setAction(Action.DEV_SEND.getAction());
          ModelData modelData = new ModelData();
          modelData.setCmd("reportValue");
          Map<String, Object> params = new HashMap<>();
          data.stream()
              .filter(runtimeResp -> runtimeResp.getDeviceId().equals(s))
              .forEach(
                  runtimeResp -> {
                    params.put("DI_" + runtimeResp.getDefineIndex(), runtimeResp.getValue());
                  });
          modelData.setParams(params);
          devSend.setData(modelData);
          proxyService.devSend(devSend);
        });
  }

  private void sleep(int timeout) {
    try {
      TimeUnit.SECONDS.sleep(timeout);
    } catch (InterruptedException ignored) {
    }
  }
}
