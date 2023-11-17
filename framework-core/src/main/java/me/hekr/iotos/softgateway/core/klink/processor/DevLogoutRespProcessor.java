package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevLogoutResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>DevLogoutRespProcessor class.</p>
 *
 * @version $Id: $Id
 */
@Component
@Slf4j
public class DevLogoutRespProcessor implements Processor<DevLogoutResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  /** {@inheritDoc} */
  @Override
  public void handle(DevLogoutResp klink) {}

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.DEV_LOGOUT_RESP;
  }
}
