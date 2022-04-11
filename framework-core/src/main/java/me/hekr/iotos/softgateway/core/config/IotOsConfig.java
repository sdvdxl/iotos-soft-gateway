package me.hekr.iotos.softgateway.core.config;

import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.utils.ParseUtil;
import me.hekr.iotos.softgateway.core.constant.Constants;
import me.hekr.iotos.softgateway.core.enums.ConnectClusterMode;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author iotos
 */
@Slf4j
@ComponentScan("me.hekr.iotos.softgateway.core")
@Configuration
public class IotOsConfig {
  @Getter private MqttConfig mqttConfig;
  @Getter private GatewayConfig gatewayConfig;

  /** mqtt 连接地址 */
  @Value("${connect.mqtt.endpoint}")
  private String endpoint;

  /** mqtt 连接超时时间 秒 */
  @Value("${connect.mqtt.connectionTimeout:10}")
  private int connectionTimeout;

  /** 心跳保持，秒 */
  @Value("${connect.mqtt.keepAliveTime}")
  private int keepAliveTime;

  /** 发送的 klink queue 大小，默认1000，超过会阻塞 */
  @Value("${connect.mqtt.klink.queue.size:1000}")
  @Getter
  private int klinkQueueSize;

  /** 是否自动回复cloudSend， true 自动回复， false 需要手动回复 */
  @Value("${connect.mqtt.autoCloudSendResp:true}")
  private boolean autoCloudSendResp;

  /** 是否本地数据发生变化才发送数据 */
  @Value("${connect.mqtt.data.changed:false}")
  private boolean dataChanged;

  /**
   * 集群模式，默认单机模式
   *
   * @see MqttConfig#clusterMode
   */
  @Value("${connect.mqtt.cluster.mode:standalone}")
  private String connectClusterMode;

  /** 网关 pk */
  @Value("${gateway.pk}")
  private String gatewayPk;

  /** 网关 devId */
  @Value("${gateway.devId}")
  private String gatewayDevId;

  /** 网关设备的密钥 */
  @Value("${gateway.devSecret}")
  private String gatewayDevSecret;

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
    mq.subscribeTopic = gw.getSubscribeTopic();
    mq.publishTopic = gw.getPublishTopic();
    mq.clusterMode = ConnectClusterMode.of(connectClusterMode);
    mq.clientId = "dev:" + gw.pk + ":" + gw.devId;
    mq.autoCloudSendResp = autoCloudSendResp;
    mq.dataChanged = dataChanged;
    // 集群模式，clientId 要加 random，此处使用时间戳防止重复
    if (mq.clusterMode.isCluster()) {
      mq.clientId += ":" + System.currentTimeMillis();
      mq.subscribeTopic = "$share/gw/" + gw.getSubscribeTopic();
    }

    mq.username = ParseUtil.HASH_METHOD + ":" + Constants.RANDOM;
    mq.password = getPassword(gw.pk, gw.devId, gw.devSecret, Constants.RANDOM).toCharArray();
    mq.connectTimeout = connectionTimeout;
    mq.keepAliveTime = keepAliveTime;

    log.info("mqtt 配置: {}", mq);
    log.info("网关配置：{}", gw);
  }

  @SneakyThrows
  private String getPassword(String pk, String devId, String devSecret, String random) {
    return Hex.encodeHexString(
        (ParseUtil.hmacSHA1Encrypt(pk + devId + devSecret + random, devSecret)));
  }
}
