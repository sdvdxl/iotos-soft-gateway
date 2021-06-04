package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.AddTopoResp;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class AddTopoRespProcessor implements Processor<AddTopoResp> {
  @Autowired private MqttService mqttService;

  @Override
  public void handle(AddTopoResp klink) {
    mqttService.noticeAddTopoSuccess();
  }

  @Override
  public Action getAction() {
    return Action.ADD_TOPO_RESP;
  }
}
