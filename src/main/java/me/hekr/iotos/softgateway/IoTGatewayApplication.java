package me.hekr.iotos.softgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tio.utils.jfinal.P;

@SpringBootApplication
@EnableScheduling
public class IoTGatewayApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(IoTGatewayApplication.class, args);

    // 获取配置文件中的相关参数
    P.use("config.properties");
  }

  //  public IoTGatewayApplication() {}
}
