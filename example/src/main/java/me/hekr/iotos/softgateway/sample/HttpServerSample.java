package me.hekr.iotos.softgateway.sample;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http server
 *
 * <p>端口配置在 resources/application.yml 中
 *
 * @author iotos
 */
@SpringBootApplication
@RestController
public class HttpServerSample {
  public static void main(String[] args) {
    SpringApplication.run(HttpServerSample.class);
  }

  @GetMapping("/")
  public Object get(Integer curPage, Integer pageSize) {
    DeviceResponse response = new DeviceResponse();

    if (curPage >= 3) {
      response.setDevices(Collections.emptyList());
    } else {
      response.setDevices(Collections.singletonList(new Device()));
    }
    return response;
  }

  @GetMapping("/items")
  public Object items(Integer curPage, Integer pageSize) {
    if (curPage >= 3) {
      return Collections.emptyList();
    }
    return Collections.singletonList(new Device());
  }
}
