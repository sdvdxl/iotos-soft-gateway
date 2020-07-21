package hekr.me.iotos.softgateway.northProxy.processor;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.GetTopoResp;
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
public class GetTopoRespProcessor implements Processor<GetTopoResp> {
  @Autowired private DeviceService deviceService;

  @Override
  public void handle(GetTopoResp klink) {
    deviceService.syncDevices(klink);
  }

  @Override
  public Action getAction() {
    return Action.GET_TOPO_RESP;
  }
}
