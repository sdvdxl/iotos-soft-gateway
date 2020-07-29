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

  @Value("${subdev.barrier.pk}")
  private String SUB_BARRIER_PK;

  @Value("${subdev.barrier.productSecret}")
  private String SUB_BARRIER_PROD_SECRET;

  @Value("${http.client.connect.host}")
  private String HTTP_URL;

  @Value("${http.client.connect.aid}")
  private String AID;

  @Value("${http.client.connect.key}")
  private String KEY;

  @Value("${http.server.port}")
  private int HTTP_PORT;

  public static String AES_KEY;

  @Value("${AES.key}")
  public void setAesKey(String aesKey) {
    AES_KEY = aesKey;
  }
}
