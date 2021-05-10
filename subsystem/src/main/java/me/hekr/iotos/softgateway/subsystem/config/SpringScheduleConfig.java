package me.hekr.iotos.softgateway.subsystem.config;

import java.util.concurrent.ScheduledExecutorService;
import me.hekr.iotos.softgateway.core.utils.ThreadPoolUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * <br>
 *
 * @author iotos
 */
@Configuration
@EnableScheduling
public class SpringScheduleConfig implements SchedulingConfigurer {

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(taskExecutor());
  }

  @Bean(destroyMethod = "shutdown")
  public ScheduledExecutorService taskExecutor() {
    return (ScheduledExecutorService)
        new ThreadPoolUtil.Builder()
            .setPrefix("spring-schedule")
            .setCore(12)
            .setMax(32)
            .setQueueSize(1000)
            .setScheduled(true)
            .build();
  }
}
