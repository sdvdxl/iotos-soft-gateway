package hekr.me.iotos.softgateway.northProxy.processor;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.GetConfigResp;
import hekr.me.iotos.softgateway.northProxy.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetConfigRespProcessor implements Processor<GetConfigResp> {

  @Autowired private DeviceService deviceService;

  @Override
  public void handle(GetConfigResp klink) {
    deviceService.getConfigResp(klink);
  }

  @Override
  public Action getAction() {
    return Action.GET_CONFIG_RESP;
  }
}
