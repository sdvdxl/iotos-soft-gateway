package hekr.me.iotos.softgateway.northProxy.processor;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.CloudSend;
import hekr.me.iotos.softgateway.northProxy.device.Device;
import hekr.me.iotos.softgateway.northProxy.device.DeviceService;
import hekr.me.iotos.softgateway.pluginAsClient.tcp.TcpClientStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudSendProcessor implements Processor<CloudSend> {

  @Autowired private TcpClientStarter tcpClientStarter;
  @Autowired private DeviceService deviceService;

  @Override
  public void handle(CloudSend klink) {
    if ("send".equals(klink.getData().getCmd())) {
      Device device = deviceService.getByPkAndDevId(klink.getDevId());
      byte[] bytes = new byte[4];
      bytes[0] = 0x02;
      bytes[1] = (byte) 2;
      bytes[2] = (byte) device.getAreaNum();
      bytes[3] = (byte) (int) klink.getData().getParams().get("PRT_SP");
      tcpClientStarter.send(bytes);
    }
  }

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
