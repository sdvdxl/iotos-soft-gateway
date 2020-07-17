package hekr.me.iotos.softgateway.northProxy.processor;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.CloudSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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
