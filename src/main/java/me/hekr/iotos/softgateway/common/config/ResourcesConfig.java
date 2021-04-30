package me.hekr.iotos.softgateway.common.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import me.hekr.iotos.softgateway.network.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Component
public class ResourcesConfig {
  @Autowired private MqttService mqttService;

  @PostConstruct
  public void init() {
    mqttService.init();
  }

  @PreDestroy
  public void close() {
    mqttService.close();
  }
}
