package hekr.me.iotos.softgateway.northProxy.processor;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.DevUpgrade;
import hekr.me.iotos.softgateway.northProxy.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author du
 *     <p>设备登录流程，包含所有设备类型
 */
@Component
@Slf4j
public class DevUpgradeProcessor implements Processor<DevUpgrade> {
  @Autowired private DeviceService deviceService;

  @Override
  public void handle(DevUpgrade klink) {
    deviceService.getConfigResp(klink);
  }

  @Override
  public Action getAction() {
    return Action.DEV_UPGRADE;
  }
}
