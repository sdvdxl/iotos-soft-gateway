package me.hekr.iotos.softgateway.core.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.ThreadPoolUtil;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author iotos */
@Slf4j
@Component
public class ResourcesConfig {
  @Autowired private MqttService mqttService;

  @PostConstruct
  public void init() {
    mqttService.init();
  }

  @PreDestroy
  public void close() {
    try {
      mqttService.close();
    } catch (Exception e) {
      log.error(e.getMessage(),e);
    }

    ThreadPoolUtil.close();
  }
}
