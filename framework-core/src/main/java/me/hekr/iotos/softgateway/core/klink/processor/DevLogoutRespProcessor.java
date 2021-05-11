package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevLogoutResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class DevLogoutRespProcessor implements Processor<DevLogoutResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  @Override
  public void handle(DevLogoutResp klink) {}

  @Override
  public Action getAction() {
    return Action.DEV_LOGOUT_RESP;
  }
}
