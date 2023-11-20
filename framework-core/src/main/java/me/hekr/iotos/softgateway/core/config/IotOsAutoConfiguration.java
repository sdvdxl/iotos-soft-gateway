package me.hekr.iotos.softgateway.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>IotOsConfig class.</p>
 *
 * @author iotos
 */
@Slf4j
@ComponentScan("me.hekr.iotos.softgateway.core")
@Configuration
public class IotOsAutoConfiguration {
    public IotOsAutoConfiguration() {
        log.info("IotOsAutoConfiguration 加载");
    }
}
