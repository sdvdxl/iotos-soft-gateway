package me.hekr.iotos.softgateway.core.common.klink.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.Klink;
import me.hekr.iotos.softgateway.core.utils.JsonUtil;
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

  public void handle(String topic, MqttMessage message, Action action) {
    if (action == null) {
      log.warn("未能解析出正确的action,data:{}", new String(message.getPayload()));
      return;
    }

    Klink klink = JsonUtil.fromBytes(message.getPayload(), action.getKlinkClass());
    log.info("klink: {}", klink);
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
