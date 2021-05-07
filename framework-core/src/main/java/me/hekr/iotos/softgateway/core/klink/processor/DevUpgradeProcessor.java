package me.hekr.iotos.softgateway.core.klink.processor;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevUpgrade;
import me.hekr.iotos.softgateway.core.klink.GetConfigResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author iotos
 * @see GetConfigRespProcessor#handle(GetConfigResp)
 */
@Component
@Slf4j
public class DevUpgradeProcessor implements Processor<DevUpgrade> {
  @Autowired private GetConfigRespProcessor getConfigRespProcessor;

  @Override
  public void handle(DevUpgrade klink) {
    handleConfig(klink);
  }

  private void handleConfig(DevUpgrade klink) {
    GetConfigResp getConfigResp = new GetConfigResp();
    BeanUtils.copyProperties(klink, getConfigResp);
    getConfigRespProcessor.handle(getConfigResp);
  }

  @Override
  public Action getAction() {
    return Action.DEV_UPGRADE;
  }
}
