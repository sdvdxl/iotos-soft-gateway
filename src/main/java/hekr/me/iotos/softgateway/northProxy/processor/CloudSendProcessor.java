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
    // TODO 处理cloudSend请求
  }

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
