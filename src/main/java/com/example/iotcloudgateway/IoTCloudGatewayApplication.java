package com.example.iotcloudgateway;

import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.server.tcp.TcpServerStarter;
import org.eclipse.paho.client.mqttv3.MqttException;

public class IoTCloudGatewayApplication {

  public static void main(String[] args) throws MqttException {
    MqttServer.mqttconnection();
    TcpServerStarter.start();
  }
}
