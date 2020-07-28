package hekr.me.iotos.softgateway.northProxy.processor;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.CloudSend;
import hekr.me.iotos.softgateway.northProxy.device.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudSendProcessor implements Processor<CloudSend> {

  @Autowired private DeviceService deviceService;

  @Override
  public void handle(CloudSend klink) {}

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
