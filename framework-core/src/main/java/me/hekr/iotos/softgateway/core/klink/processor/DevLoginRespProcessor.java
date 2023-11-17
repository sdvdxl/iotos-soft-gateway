package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevLoginResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>DevLoginRespProcessor class.</p>
 *
 * @version $Id: $Id
 */
@Component
@Slf4j
public class DevLoginRespProcessor implements Processor<DevLoginResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  /** {@inheritDoc} */
  @Override
  public void handle(DevLoginResp klink) {}

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.DEV_LOGIN_RESP;
  }
}
