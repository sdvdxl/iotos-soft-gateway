package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevSendResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>DevSendRespProcessor class.</p>
 *
 * @version $Id: $Id
 */
@Component
@Slf4j
public class DevSendRespProcessor implements Processor<DevSendResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  /** {@inheritDoc} */
  @Override
  public void handle(DevSendResp klink) {}

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.DEV_SEND_RESP;
  }
}
