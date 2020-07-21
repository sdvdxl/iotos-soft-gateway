package hekr.me.iotos.softgateway.northProxy.device;

import static java.util.stream.Collectors.toList;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.GateControlReq;
import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.CloudSend;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${subdev.barrier.pk}")
  private String barrierPk;

  @Value("${subdev.barrier.productSecret}")
  private String barrierProductSecret;

  /** key:devId */
  private static final ConcurrentMap<String, Device> IOT_DEVICE_MAP = new ConcurrentHashMap<>();

  /** key:channelId@deviceType */
  private static final ConcurrentMap<String, Device> SUBSYSTEM_DEVICE_MAP =
      new ConcurrentHashMap<>();

  public static Device getByDevId(String devId) {
    return IOT_DEVICE_MAP.getOrDefault(devId, null);
  }

  public static Device getByIdAndType(String Id, DeviceType deviceType) {
    return SUBSYSTEM_DEVICE_MAP.getOrDefault(Id + "@" + deviceType.name(), null);
  }

  public synchronized void updateDevices(List<Device> deviceList) {
    // 更新本地缓存
    IOT_DEVICE_MAP.clear();
    SUBSYSTEM_DEVICE_MAP.clear();
    for (Device device : deviceList) {
      IOT_DEVICE_MAP.put(device.getDevId(), device);
      if (device.getDeviceType() == DeviceType.BARRIER) {
        SUBSYSTEM_DEVICE_MAP.put(
            device.getChannelID() + "@" + device.getDeviceType().name(), device);
      }
    }
    proxyService.getTopo();
  }

  @Scheduled(fixedDelay = 60 * 1000)
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

  public void deviceCommand(CloudSend cloudSend) {
    Device device = getByDevId(cloudSend.getDevId());
    if (DeviceType.BARRIER.equals(device.getDeviceType())) {
      barrierCommand(cloudSend);
    }
  }

  public void barrierCommand(CloudSend cloudSend) {
    switch (cloudSend.getData().getCmd()) {
      case "gateControl":
        gateControl(cloudSend);
        break;
      default:
        break;
    }
  }

  private void gateControl(CloudSend cloudSend) {
    Map<String, Object> params = cloudSend.getData().getParams();
    GateControlReq gateControlReq = new GateControlReq();
    Device device = getByDevId(cloudSend.getDevId());
    gateControlReq.setChannelID(device.getChannelID());
    gateControlReq.setControlType((int) params.get("controlType"));
    if (params.containsKey("reason")) {
      gateControlReq.setReason((String) params.get("reason"));
    }
    if (params.containsKey("userID")) {
      gateControlReq.setReason((String) params.get("userID"));
    }
    byte[] bytes = httpClient.gateControl(gateControlReq);
    BaseResp baseResp = JsonUtil.fromBytes(bytes, BaseResp.class);

    // 返回数据上报
    DevSend devSend = new DevSend();
    devSend.setDevId(device.getDevId());
    devSend.setPk(device.getPk());
    ModelData data = new ModelData();
    data.setCmd("reportCallback");
    Map<String, Object> resp = new HashMap<>();
    resp.put("resCode", baseResp.getResCode());
    resp.put("resMsg", baseResp.getResMsg());
    data.setParams(resp);
    devSend.setData(data);
    devSend.setAction(Action.DEV_SEND.getAction());
    proxyService.devSend(devSend);
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
        if (DeviceType.BARRIER.equals(device.getDeviceType())) {
          device.setPk(barrierPk);
          device.setProductSecret(barrierProductSecret);
        }
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
    List<String> gatewaySubDevs = klink.getSubs().stream().map(Dev::getDevId).collect(toList());
    doSyncDevice(gatewaySubDevs);
    //    updateDeviceStatus();
    //    doSyncDeviceInfo(allDevices);
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
            try {
              proxyService.register(
                  device.getPk(), d, device.getProductSecret(), device.getDevName());
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
            proxyService.addDev(device.getPk(), d, null);
          });

      sleep(5);
      // 登录
      log.info("发送登录信息");
      addTopoList.forEach(
          d -> {
            Device device = IOT_DEVICE_MAP.get(d);
            proxyService.devLogin(device.getPk(), d);
          });
    }

    // 找出gatewaySubDevs中存在，但是remoteDevs中不存在的，进行删除拓扑
    List<String> delTopoList = CollectionUtil.subtractToList(gatewaySubDevs, iotDeviceIdList);
    log.info("需要删除的设备：{}", delTopoList);
    delTopoList.forEach(
        d -> {
          Device device = IOT_DEVICE_MAP.get(d);
          proxyService.delDev(device.getPk(), d);
        });
  }

  private void sleep(int timeout) {
    try {
      TimeUnit.SECONDS.sleep(timeout);
    } catch (InterruptedException ignored) {
    }
  }
}
