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

  // tcp://MQTT安装的服务器地址:MQTT定义的端口号
  // 进入产品中心-产品开发-智慧消防平台软网关，"MQTT接入方式"栏目即可查询
  @Value("${iotos.host}")
  private String HOST;
  // 软网关的产品pk，进入产品中心-设备管理-智慧消防平台软网关，"产品pk"栏目即可查询
  @Value("${gateway.DEV_PK}")
  private String DEV_PK;
  // 软网关的设备id，进入产品中心-设备管理-智慧消防平台软网关，"产品id"栏目即可查询
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

  @Value("${http.server.port}")
  private int HTTP_PORT;

  public static String AES_KEY;

  @Value("${AES.key}")
  public void setAesKey(String aesKey){
    AES_KEY = aesKey;
  }

}
