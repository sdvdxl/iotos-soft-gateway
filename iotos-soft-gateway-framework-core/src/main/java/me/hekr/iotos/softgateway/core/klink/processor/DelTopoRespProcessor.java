package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DelTopoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class DelTopoRespProcessor implements Processor<DelTopoResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  @Override
  public void handle(DelTopoResp klink) {}

  @Override
  public Action getAction() {
    return Action.DEL_TOPO_RESP;
  }
}
