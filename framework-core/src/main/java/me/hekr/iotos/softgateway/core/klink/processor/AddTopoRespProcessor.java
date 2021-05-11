package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.AddTopoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
@Slf4j
public class AddTopoRespProcessor implements Processor<AddTopoResp> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  @Override
  public void handle(AddTopoResp klink) {}

  @Override
  public Action getAction() {
    return Action.ADD_TOPO_RESP;
  }
}
