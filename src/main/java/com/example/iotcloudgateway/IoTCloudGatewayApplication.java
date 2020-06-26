package com.example.iotcloudgateway;

import com.example.iotcloudgateway.client.tcp.TcpClientStarter;
import com.example.iotcloudgateway.client.udp.UdpClientStarter;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.server.http.HttpServerInit;
import com.example.iotcloudgateway.server.tcp.TcpServerStarter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.tio.utils.jfinal.P;

public class IoTCloudGatewayApplication {

  public static void main(String[] args) throws Exception {
    MqttServer.mqttconnection();
    //    TcpServerStarter.start();
    P.use("app.properties");
    HttpServerInit.init();
    // 若要启用TCP server则将下行注释打开
    //    TcpServerStarter.start();

    //    TcpClientStarter.start();
//    TcpServerStarter.start();
//    TcpClientStarter.start();
    UdpClientStarter.start();
  }

  public IoTCloudGatewayApplication() {}
}
