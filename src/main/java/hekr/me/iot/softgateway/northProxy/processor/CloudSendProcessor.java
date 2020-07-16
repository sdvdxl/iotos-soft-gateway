package hekr.me.iot.softgateway.northProxy.processor;

import hekr.me.iot.softgateway.common.enums.Action;
import hekr.me.iot.softgateway.common.klink.CloudSend;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudSendProcessor implements Processor<CloudSend> {

  @Override
  public void handle(CloudSend klink) {
    //    deviceService.deviceCommand(klink);
    log.info(klink.toString());
  }

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}