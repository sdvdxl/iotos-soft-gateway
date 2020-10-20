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

  @Value("${connect.mqtt.endpoint}")
  private String HOST;

  @Value("${gateway.pk}")
  private String DEV_PK;

  @Value("${gateway.devId}")
  private String DEV_ID;

  @Value("${gateway.devSecret}")
  private String DEV_SECRET;

  @Value("${gateway.sub.pk}")
  private String SUB_PK;

  @Value("${gateway.sub.productSecret}")
  private String SUB_PRODUCT_SECRET;

  @Value("${tcp.client.connect.ip}")
  private String TCP_CONNECT_IP;

  @Value("${tcp.client.connect.port}")
  private int TCP_CONNECT_PORT;

  @Value("${tcp.server.port}")
  private int TCP_SERVER_PORT;

  @Value("${http.client.connect.host}")
  private String HTTP_URL;

  @Value("${http.client.connect.aid}")
  private String AID;

  @Value("${http.client.connect.key}")
  private String KEY;

  @Value("${http.server.port}")
  private int HTTP_PORT;
}
