package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DelTopoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>DelTopoRespProcessor class.</p>
 *
 * @version $Id: $Id
 */
@Component
@Slf4j
public class DelTopoRespProcessor implements Processor<DelTopoResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  /** {@inheritDoc} */
  @Override
  public void handle(DelTopoResp klink) {}

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.DEL_TOPO_RESP;
  }
}
