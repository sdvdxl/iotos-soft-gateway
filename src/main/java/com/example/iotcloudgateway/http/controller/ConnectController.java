package com.example.iotcloudgateway.http.controller;

import com.example.iotcloudgateway.SubMqtt.MqttConnect;
import com.example.iotcloudgateway.SubMqtt.MqttServer;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.Klink;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // 相当于@Controller+@RequestBody
@ResponseBody
@RequestMapping("/dev")
public class ConnectController {
  @Autowired private MqttServer mqttServer;

  @PostMapping("/push")
  public void push(@RequestBody Klink klink) throws MqttException {
    String s = klink.toString(); // klink转换成string
    mqttServer.publish(JsonUtil.toBytes(klink));
  }
}
