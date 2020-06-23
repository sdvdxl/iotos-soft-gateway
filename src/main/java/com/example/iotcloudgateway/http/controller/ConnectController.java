package com.example.iotcloudgateway.http.controller;

import com.example.iotcloudgateway.subMqtt.MqttServer;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // 相当于@Controller+@RequestBody
@ResponseBody
@RequestMapping("/dev")
public class ConnectController {
  @Autowired private MqttServer mqttServer;

  @PostMapping("/push")
  public void push(@RequestBody KlinkDev klinkDev) throws MqttException {
    mqttServer.publish(JsonUtil.toBytes(klinkDev));
  }
}
