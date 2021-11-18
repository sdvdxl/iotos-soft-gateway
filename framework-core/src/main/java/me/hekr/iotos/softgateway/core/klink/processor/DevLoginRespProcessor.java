package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevLoginResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class DevLoginRespProcessor implements Processor<DevLoginResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  @Override
  public void handle(DevLoginResp klink) {}

  @Override
  public Action getAction() {
    return Action.DEV_LOGIN_RESP;
  }
}
