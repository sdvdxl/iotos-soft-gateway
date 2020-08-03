package hekr.me.iotos.softgateway.northProxy.device;

import static java.util.stream.Collectors.toList;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.EnergyStatDataReq;
import hekr.me.iotos.softgateway.common.dto.EnergyStatDataResp;
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
      proxyConnectService.connect();
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

  @Scheduled(fixedDelay = 10 * 1000)
  public void scheduleGetTotal() {
    if (proxyConnectService.isConnected()) {
      log.info("正在定时发送日消耗统计");
      IOT_DEVICE_MAP.values().stream()
          .forEach(
              device -> {
                EnergyStatDataReq energyStatDataReq = new EnergyStatDataReq();
                energyStatDataReq.setDateRange("Day");
                energyStatDataReq.setEnergyType("01");
                energyStatDataReq.setParentType("Device");
                energyStatDataReq.setParentId(device.getDeviceId());
                // 获取时间
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
                LocalDateTime endDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
                Date begin = new Date(startDay.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                Date end = new Date(endDay.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                energyStatDataReq.setBeginDate(format.format(begin));
                energyStatDataReq.setEndDate(format.format(end));
                BaseResp<List<EnergyStatDataResp>> energyStatData =
                    httpClient.getEnergyStatData(energyStatDataReq);
                if (energyStatData.getData() != null) {
                  List<EnergyStatDataResp> data = energyStatData.getData();
                  if (data.size() > 0) {
                    EnergyStatDataResp energyStatDataResp = data.get(0);
                    DevSend devSend = new DevSend();
                    devSend.setPk(device.getPk());
                    devSend.setDevId(device.getDevId());
                    devSend.setAction(Action.DEV_SEND.getAction());
                    ModelData modelData = new ModelData();
                    modelData.setCmd("reportValue");
                    Map<String, Object> params = new HashMap<>();
                    params.put(
                        "DI_T_" + energyStatDataResp.getDefineIndex(),
                        energyStatDataResp.getCurrentAmount());
                    modelData.setParams(params);
                    devSend.setData(modelData);
                    proxyService.devSend(devSend);
                  }
                }
              });
    } else {
      log.warn("等待mqtt连接，发送 getTopo,同步设备");
      return;
    }
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
            if (device == null) {
              log.warn("【进行注册操作】设备不存在，设备pk和设备ID为：{}", d);
              return;
            }
            try {
              proxyService.register(
                  device.getPk(),
                  device.getDevId(),
                  device.getProductSecret(),
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
              log.warn("【进行添加拓扑关系操作】设备不存在，设备pk和设备ID为：{}", d);
              return;
            }
            proxyService.addDev(device.getPk(), device.getDevId(), null);
          });

      sleep(5);
      // 登录
      log.info("发送登录信息");
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            if (device == null) {
              log.warn("【进行发送登录信息操作】设备不存在，设备pk和设备ID为：{}", d);
              return;
            }
            proxyService.devLogin(device.getPk(), device.getDevId());
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

  /** 更新设备状态 */
  private void updateDeviceStatus() {
    String[] deviceIds =
        IOT_DEVICE_MAP.values().stream().map(Device::getDeviceId).toArray(String[]::new);
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
    // 若参数的errorCode为0则说明设备在线
    List<String> onlineList =
        data.stream()
            .filter(runtimeResp -> runtimeResp.getErrorCode() == 0)
            .map(RuntimeResp::getDeviceId)
            .distinct()
            .collect(toList());

    // 对在线设备进行登录操作
    onlineList.forEach(
        s -> {
          Device device = getById(s);
          if (device == null) {
            log.warn("【进行登录操作】设备不存在，设备pk和设备ID为：{}", s);
            return;
          }
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
                    params.put(
                        "DI_" + runtimeResp.getDefineIndex(), formatValue(runtimeResp.getValue()));
                  });
          params.put("online", 1);
          modelData.setParams(params);
          devSend.setData(modelData);
          proxyService.devSend(devSend);
        });

    // 获取所有未在线的设备进行下线
    List<String> offlineList = CollectionUtil.subtractToList(Arrays.asList(deviceIds), onlineList);
    offlineList.forEach(
        s -> {
          Device device = getById(s);
          if (device == null) {
            log.warn("【进行离线操作】设备不存在，设备pk和设备ID为：{}", s);
            return;
          }
          DevSend devSend = new DevSend();
          devSend.setPk(device.getPk());
          devSend.setDevId(device.getDevId());
          devSend.setAction(Action.DEV_SEND.getAction());
          ModelData modelData = new ModelData();
          modelData.setCmd("reportValue");
          Map<String, Object> params = new HashMap<>();
          params.put("online", 0);
          modelData.setParams(params);
          devSend.setData(modelData);
          proxyService.devSend(devSend);

          proxyService.devLogout(device.getPk(), device.getDevId());
        });
  }

  /** 格式化数据，整数则返回整数 */
  private Object formatValue(double value) {
    if (value == 1.0 || value == 0.0) {
      return (int) value;
    } else {
      return value;
    }
  }

  private void sleep(int timeout) {
    try {
      TimeUnit.SECONDS.sleep(timeout);
    } catch (InterruptedException ignored) {
    }
  }
}
