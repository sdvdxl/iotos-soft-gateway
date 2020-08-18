package hekr.me.iotos.softgateway.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiatao
 * @date 2020/7/9
 */
@Configuration
@Data
public class ProxyConfig {

  @Value("${iotos.host}")
  private String HOST;

  @Value("${gateway.DEV_PK}")
  private String DEV_PK;

  @Value("${gateway.DEV_ID}")
  private String DEV_ID;

  @Value("${gateway.devSecret}")
  private String DEV_SECRET;

  @Value("${tcp.client.connect.ip}")
  private String TCP_CONNECT_IP;

  @Value("${tcp.client.connect.port}")
  private int TCP_CONNECT_PORT;


  @Value("${http.client.connect.host}")
  private String HTTP_URL;

  @Value("${http.client.connect.aid}")
  private String AID;

  @Value("${http.client.connect.key}")
  private String KEY;

  @Value("${http.server.port}")
  private int HTTP_PORT;
}
