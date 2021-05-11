package me.hekr.iotos.softgateway.subsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** @author iotos */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@SpringBootApplication
@EnableScheduling
public class IoTGatewayApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(IoTGatewayApplication.class, args);
  }
}
