package me.hekr.iotos.softgateway.core.enums;

import java.util.Arrays;
import lombok.Getter;

/**
 * 网关集群模式 STANDALONE 单机模式，即同一个网关只允许一个客户端登录 CLUSTER和BROADCAST都是开启集群模式， 即允许该网关（相同
 * pk，devId）同时登录多个客户端；都能首发消息； 构造的 clientId 一样
 *
 * <p>CLUSTER模式，给设备的同一条消息，只有1个客户端能收到（随机的，不能指定是哪个客户端，即使是同一个客户端发送的请求）
 *
 * <p>BROADCAST 广播模式，给设备发送的同一条消息，所有的客户端都能收到，即使这条回复不是该客户端发送的
 *
 * @author iot
 */
public enum ConnectClusterMode {
  /** 单机模式 */
  STANDALONE("standalone", "单机模式"),
  /** 集群模式 */
  CLUSTER("cluster", "集群模式"),

  /** 广播模式 */
  BROADCAST("broadcast", "广播模式");

  @Getter private final String mode;
  @Getter private final String desc;

  ConnectClusterMode(String mode, String desc) {
    this.mode = mode;
    this.desc = desc;
  }

  /**
   * 从字符串 mode 中返回映射
   *
   * @param mode standalone, cluster, broadcast
   * @return GatewayClusterMode，没有关系返回 默认 STANDALONE
   */
  public static ConnectClusterMode of(String mode) {
    return Arrays.stream(ConnectClusterMode.values())
        .filter(v -> v.mode.equalsIgnoreCase(mode))
        .findAny()
        .orElse(STANDALONE);
  }

  public boolean isCluster() {
    return this != STANDALONE;
  }

  @Override
  public String toString() {
    return "mode='" + mode + '\'' + ", desc='" + desc + '\'';
  }
}
