package me.hekr.iotos.softgateway.core.config;

import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import me.hekr.iotos.softgateway.core.constant.Constants;
import me.hekr.iotos.softgateway.core.utils.ParseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author iotos
 * @date 2020/7/9
 */
@Configuration
public class IotOsConfig {
  @Getter private MqttConfig mqttConfig;
  @Getter private GatewayConfig gatewayConfig;

  @Value("${connect.mqtt.endpoint}")
  private String endpoint;

  @Value("${connect.mqtt.connectionTimeout}")
  private int connectionTimeout;

  @Value("${connect.mqtt.keepAliveTime}")
  private int keepAliveTime;

  @Value("${gateway.pk}")
  private String gatewayPk;

  @Value("${gateway.devId}")
  private String gatewayDevId;

  @Value("${gateway.devSecret}")
  private String gatewayDevSecret;

  @Value("${tcp.client.connect.ip}")
  @Getter
  private String tcpConnectIp;

  @Value("${tcp.client.connect.port}")
  @Getter
  private int tcpConnectPort;

  @Value("${tcp.server.port}")
  @Getter
  private int tcpServerPort;

  @Value("${http.client.connect.host}")
  @Getter
  private String httpUrl;

  @Value("${http.client.connect.aid}")
  @Getter
  private String aid;

  @Value("${http.client.connect.key}")
  @Getter
  private String key;

  @Value("${http.server.port}")
  @Getter
  private int httpPort;

  @PostConstruct
  public void init() {
    gatewayConfig = new GatewayConfig();
    gatewayConfig.pk = gatewayPk;
    gatewayConfig.devId = gatewayDevId;
    gatewayConfig.devSecret = gatewayDevSecret;

    mqttConfig = new MqttConfig();
    MqttConfig mq = mqttConfig;
    GatewayConfig gw = gatewayConfig;
    mq.endpoint = endpoint;
    mq.clientId = "dev:" + gw.pk + ":" + gw.devId;
    mq.username = Constants.HASH_METHOD + ":" + Constants.RANDOM;
    mq.password = getPassword(gw.pk, gw.devId, gw.devSecret, Constants.RANDOM).toCharArray();
    mq.connectTimeout = connectionTimeout;
    mq.keepAliveTime = keepAliveTime;
  }

  @SneakyThrows
  private String getPassword(String pk, String devId, String devSecret, String random) {
    return ParseUtil.parseByte2HexStr(
        (ParseUtil.HmacSHA1Encrypt(pk + devId + devId + random, devSecret)));
  }
}
