package me.hekr.iotos.softgateway.core.common.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.RegisterResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class RegisterRespProcessor implements Processor<RegisterResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  @Override
  public void handle(RegisterResp klink) {}

  @Override
  public Action getAction() {
    return Action.REGISTER_RESP;
  }
}
