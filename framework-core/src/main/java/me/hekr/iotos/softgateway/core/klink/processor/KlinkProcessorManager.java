package me.hekr.iotos.softgateway.core.klink.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.config.IotOsAutoConfiguration;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.Klink;
import me.hekr.iotos.softgateway.core.klink.KlinkResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>KlinkProcessorManager class.</p>
 *
 * @version $Id: $Id
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class KlinkProcessorManager {
  private final Map<Action, Processor> processorMap;
  @Autowired private KlinkService klinkService;
  @Autowired private IotOsAutoConfiguration iotOsAutoConfiguration;

  /**
   * <p>Constructor for KlinkProcessorManager.</p>
   *
   * @param processorList a {@link java.util.List} object.
   */
  public KlinkProcessorManager(List<Processor> processorList) {

    processorMap =
        processorList.stream().collect(Collectors.toMap(Processor::getAction, Function.identity()));
  }

  /**
   * <p>getProcessor.</p>
   *
   * @param action a {@link me.hekr.iotos.softgateway.core.enums.Action} object.
   * @return a {@link me.hekr.iotos.softgateway.core.klink.processor.Processor} object.
   */
  public Processor getProcessor(Action action) {
    Processor processor = processorMap.get(action);
    return processor == null ? processorMap.get(Action.NOT_SUPPORT) : processor;
  }

  /**
   * <p>handle.</p>
   *
   * @param topic a {@link java.lang.String} object.
   * @param message a {@link org.eclipse.paho.client.mqttv3.MqttMessage} object.
   * @param action a {@link me.hekr.iotos.softgateway.core.enums.Action} object.
   */
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
}
