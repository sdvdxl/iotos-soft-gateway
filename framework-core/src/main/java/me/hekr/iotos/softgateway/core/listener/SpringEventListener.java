package me.hekr.iotos.softgateway.core.listener;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.ResourcesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringEventListener {
  @Autowired private ResourcesConfig resourcesConfig;

  @EventListener(value = {ApplicationReadyEvent.class})
  public void onApplicationStartedEvent(ApplicationReadyEvent event) {
    log.info("应用启动成功， 开始链接MQTT，初始化业务流程");
    // 只是为了看清楚日志
    for (int i = 0; i < 5; i++) {
      log.info("业务初始化中...");
      ThreadUtil.safeSleep(1000);
    }
    resourcesConfig.init();
  }
}
