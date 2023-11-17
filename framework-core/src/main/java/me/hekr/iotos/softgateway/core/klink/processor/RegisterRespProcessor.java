package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.RegisterResp;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>RegisterRespProcessor class.</p>
 *
 * @version $Id: $Id
 */
@Component
@Slf4j
public class RegisterRespProcessor implements Processor<RegisterResp> {
  @Autowired private MqttService mqttService;

  /** {@inheritDoc} */
  @Override
  public void handle(RegisterResp klink) {
    // 不成功打印信息
    if (!klink.isSuccess()) {
      log.error(
          "============== 注册失败 ============ \n发送消息：{}, 返回消息：{}",
          JsonUtil.toJson(mqttService.peekRegisterMessage()),
          JsonUtil.toJson(klink));
    }

    // 不管成功成功，解锁，让其他注册信息发送
    mqttService.noticeRegisterSuccess();
  }

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.REGISTER_RESP;
  }
}
