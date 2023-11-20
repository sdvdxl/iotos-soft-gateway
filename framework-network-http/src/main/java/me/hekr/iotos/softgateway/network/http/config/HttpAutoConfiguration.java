package me.hekr.iotos.softgateway.network.http.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author du
 */
@Configuration
@Slf4j
@ComponentScan("me.hekr.iotos.softgateway.network.http")
public class HttpAutoConfiguration {
  public HttpAutoConfiguration() {
    log.info("加载http config");
  }
}
