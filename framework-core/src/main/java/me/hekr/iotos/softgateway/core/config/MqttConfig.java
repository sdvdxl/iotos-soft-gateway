package me.hekr.iotos.softgateway.core.config;

import lombok.Data;
import me.hekr.iotos.softgateway.core.enums.ConnectClusterMode;

/**
 * <p>MqttConfig class.</p>
 *
 * @author iotos
 * @version $Id: $Id
 */
@Data
public class MqttConfig {

  /** 缓存大小 */
  protected int cacheSize = 100_0000;

  protected int cacheExpireSeconds = 3600;
  protected String endpoint;
  protected String username;
  protected char[] password;
  protected String clientId;

  /** 连接超时时间，单位秒 */
  protected int connectTimeout;

  /** 心跳时间，单位秒 */
  protected int keepAliveTime;

  protected boolean autoCloudSendResp;

  /**
   * 集群模式
   *
   * <p>true 开启集群模式，即允许该网关（相同 pk，devId）同时登录多个客户端；都能首发消息； 构造的 clientId 一样
   *
   * <p>false 关闭集群模式，clientId 都是一样的；重复登录会相互顶替上下线，即只允许一个客户端在线
   */
  protected ConnectClusterMode clusterMode;

  protected String publishTopic;
  protected String subscribeTopic;

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "MqttConfig{"
        + "endpoint='"
        + endpoint
        + '\''
        + ", username='"
        + username
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + ", connectTimeout="
        + connectTimeout
        + ", keepAliveTime="
        + keepAliveTime
        + ", clusterMode="
        + clusterMode
        + ", publishTopic='"
        + publishTopic
        + '\''
        + ", subscribeTopic='"
        + subscribeTopic
        + '\''
        + '}';
  }
}
