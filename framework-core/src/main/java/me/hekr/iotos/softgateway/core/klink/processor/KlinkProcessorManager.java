package me.hekr.iotos.softgateway.core.klink.processor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;
import me.hekr.iotos.softgateway.core.klink.Klink;
import me.hekr.iotos.softgateway.core.klink.KlinkResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author iotos */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class KlinkProcessorManager {
  private final Map<Action, Processor> processorMap;
  @Autowired private KlinkService klinkService;
  @Autowired private IotOsConfig iotOsConfig;

  public KlinkProcessorManager(List<Processor> processorList) {

    processorMap =
        processorList.stream().collect(Collectors.toMap(Processor::getAction, Function.identity()));
  }

  public Processor getProcessor(Action action) {
    Processor processor = processorMap.get(action);
    return processor == null ? processorMap.get(Action.NOT_SUPPORT) : processor;
  }

  @SuppressWarnings("unchecked")
  public void handle(String topic, MqttMessage message, Action action) {
    if (action == null) {
      log.warn("未能解析出正确的action,data:{}", new String(message.getPayload()));
      return;
    }

    Klink klink = JsonUtil.fromBytes(message.getPayload(), action.getKlinkClass());
    if (klink instanceof KlinkResp) {
      if (!((KlinkResp) klink).isSuccess()) {
        log.error("收到mqtt返回消息不成功，消息： {}", new String(message.getPayload()));
      }
    }

    handleDeviceError(klink);

    log.debug("klink: {}", klink);

    Processor processor = getProcessor(action);
    if (processor == null) {
      log.warn("未知命令: {}, data:{}", action.getAction(), new String(message.getPayload()));
      return;
    }
    try {
      processor.handle(klink);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void handleDeviceError(Klink klink) {
    if (klink instanceof KlinkResp) {
      KlinkResp resp = (KlinkResp) klink;
      boolean isDeviceNotExist = resp.getCode() == ErrorCode.DEVICE_NOT_EXIST.getCode();
      boolean isTopoNotExist = resp.getCode() == ErrorCode.DEVICE_TOPO_NOT_EXIST.getCode();
      if (isDeviceNotExist || isTopoNotExist) {
        String pk;
        String devId;
        if (isDeviceNotExist) {
          if (resp.getParams() == null) {
            // device not exist, pk:aaba4d89764c45e7a410510012f36ae7, devId:demo_subsystem_002
            String[] args = resp.getDesc().substring("device not exist, ".length()).split(", ");
            pk = args[0].split(":")[1];
            devId = args[1].split(":")[1];
          } else {
            pk = (String) resp.getParams().get("subPk");
            devId = (String) resp.getParams().get("subDevId");
          }
        } else {
          pk = (String) resp.getParams().get("subPk");
          devId = (String) resp.getParams().get("subDevId");
        }

        Optional<DeviceRemoteConfig> optional = DeviceRemoteConfig.getByPkAndDevId(pk, devId);
        if (optional.isPresent()) {
          if (isDeviceNotExist) {
            klinkService.addDev(pk, devId, optional.get().getDevName());
          } else {
            klinkService.addTopo(pk, devId);
          }
        } else {
          // 一般不应该走到这里，除非在这时候非常凑巧的删除了这个远程配置
          log.warn("设备没有映射在远程配置, pk:{}, devId:{}", pk, devId);
        }
      }
    }
  }
}
