package com.example.iotcloudgateway;

import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.server.tcp.TcpServerStarter;
import org.tio.utils.jfinal.P;

public class IoTCloudGatewayApplication {

  public static void main(String[] args) throws Exception {
    MqttServer.mqttconnection();
    P.use("app.properties");
    // 若要启用http则将下行注释打开
    //    HttpServerInit.init();
    // 若要启用TCP client则将下行注释打开
    //    TcpClientStarter.start();
    // 若要启用TCP server则将下行注释打开
    TcpServerStarter.start();
    // 若要启用UDP client则将下行注释打开
    //    UdpClientStarter.start();
    // 若要启用UDP server则将下行注释打开
    //    UdpServerStarter.start();
  }

  public IoTCloudGatewayApplication() {}
}
