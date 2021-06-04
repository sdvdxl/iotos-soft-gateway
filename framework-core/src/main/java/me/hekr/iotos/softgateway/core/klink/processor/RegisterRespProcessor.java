package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.RegisterResp;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class RegisterRespProcessor implements Processor<RegisterResp> {
  @Autowired private MqttService mqttService;

  @Override
  public void handle(RegisterResp klink) {
    if (klink.isSuccess()) {
      mqttService.noticeRegisterSuccess();
    }
  }

  @Override
  public Action getAction() {
    return Action.REGISTER_RESP;
  }
}
