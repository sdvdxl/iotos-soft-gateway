package me.hekr.iotos.softgateway.network.http;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Server {
  public static void main(String[] args) {
    SpringApplication.run(Server.class);
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
}
