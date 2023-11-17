package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.AddTopoResp;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>AddTopoRespProcessor class.</p>
 *
 * @version $Id: $Id
 */
@Component
@Slf4j
public class AddTopoRespProcessor implements Processor<AddTopoResp> {
  @Autowired private MqttService mqttService;

  /** {@inheritDoc} */
  @Override
  public void handle(AddTopoResp klink) {
    // 不成功打印信息
    if (!klink.isSuccess()) {
      log.error(
          "============== 添加拓扑失败 ============ \n发送消息：{}, 返回消息：{}",
          JsonUtil.toJson(mqttService.peekAddTopoMessage()),
          JsonUtil.toJson(klink));
    }

    // 不管成功成功，解锁，让其他添加拓扑信息发送
    mqttService.noticeAddTopoSuccess();
  }

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.ADD_TOPO_RESP;
  }
}
