package me.hekr.iotos.softgateway.core.klink.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.Klink;
import me.hekr.iotos.softgateway.core.klink.RegisterResp;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

/** @author iotos */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class KlinkProcessorManager {

  private final Map<Action, Processor> processorMap;

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
    log.debug("klink: {}", klink);
    if (action == Action.REGISTER_RESP) {
      RegisterResp resp = (RegisterResp) klink;
      if (resp.isSuccess()){
        MqttService.noticeRegisterSuccess();
      }
    } else if (action == Action.ADD_TOPO_RESP) {
      MqttService.noticeAddTopoSuccess();
    }
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
}
