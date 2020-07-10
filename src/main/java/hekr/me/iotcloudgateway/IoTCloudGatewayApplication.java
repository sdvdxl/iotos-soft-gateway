package hekr.me.iotcloudgateway;

import hekr.me.iotcloudgateway.mqtt.MqttCallbackService;
import hekr.me.iotcloudgateway.mqtt.MqttServer;
import hekr.me.iotcloudgateway.mqtt.processor.CloudSendProcessor;
import hekr.me.iotcloudgateway.server.tcp.TcpServerStarter;
import org.tio.utils.jfinal.P;

public class IoTCloudGatewayApplication {

  public static void main(String[] args) throws Exception {
    P.use("config.properties");
    MqttServer.init();

    MqttCallbackService.processorManager.register(new CloudSendProcessor());
    // 若要启用http则将下行注释打开
    //    HttpServerInit.init();
    // 若要启用TCP client则将下行注释打开
    //   TcpClientStarter.start();
    // 若要启用TCP server则将下行注释打开
//    TcpServerStarter.start();
    // 若要启用UDP client则将下行注释打开
    //   UdpClientStarter.start();
    // 若要启用UDP server则将下行注释打开
    //    UdpServerStarter.start();
  }

  public IoTCloudGatewayApplication() {}
}
