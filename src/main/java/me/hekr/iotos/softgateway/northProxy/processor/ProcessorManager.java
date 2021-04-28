package me.hekr.iotos.softgateway.northProxy.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.enums.Action;
import me.hekr.iotos.softgateway.common.klink.Klink;
import me.hekr.iotos.softgateway.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessorManager {

  private final Map<Action, Processor> processorMap;

  public ProcessorManager(List<Processor> processorList) {
    processorMap =
        processorList.stream().collect(Collectors.toMap(Processor::getAction, Function.identity()));
  }

  public void register(Processor processor) {
    processorMap.put(processor.getAction(), processor);
  }

  public void unRegister(Processor processor) {
    processorMap.remove(processor.getAction());
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
    processor.handle(klink);
  }
}