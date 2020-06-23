package com.example.iotcloudgateway;

import com.example.iotcloudgateway.mqtt.MqttConnect;
import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.tcp.TcpServerListener;
import com.example.iotcloudgateway.tcp.TcpServerMsgHandler;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;

import java.net.SocketException;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

// @SpringBootApplication
// @EnableFeignClients
public class IoTCloudGatewayApplication {
  static ServerAioHandler aioHandler = new TcpServerMsgHandler();
  static ServerAioListener aioListener = new TcpServerListener();
  public static ServerTioConfig serverTioConfig = new ServerTioConfig(aioHandler, aioListener);
  static TioServer tioServer = new TioServer(serverTioConfig); // 可以为空

  static String serverIp = null;
  static int serverPort = 7000;

  public static void main(String[] args) throws IOException, MqttException {
    //    SpringApplication.run(IoTCloudGatewayApplication.class, args);
    MqttServer.mqttconnection();
    tioServer.start(serverIp, serverPort);
  }
}
