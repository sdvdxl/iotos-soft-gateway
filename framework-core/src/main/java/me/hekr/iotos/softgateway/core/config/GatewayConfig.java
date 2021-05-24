package me.hekr.iotos.softgateway.core.config;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.GatewayClusterMode;

/** @author iotos */
@Getter
@ToString
public class GatewayConfig {
  protected String pk;
  protected String devId;
  protected String devSecret;
  /**
   * 集群模式
   *
   * <p>true 开启集群模式，即允许该网关（相同 pk，devId）同时登录多个客户端；都能首发消息； 构造的 clientId 一样
   *
   * <p>false 关闭集群模式，clientId 都是一样的；重复登录会相互顶替上下线，即只允许一个客户端在线
   */
  protected GatewayClusterMode clusterMode;

  private String getTopicPrefix() {
    if (clusterMode.isCluster()) {
      return "$share/gateway/";
    }
    return "";
  }

  public String getUpTopic() {
    assertValuesSet();
    return "up/dev/" + pk + "/" + devId;
  }

  private void assertValuesSet() {
    Assert.notBlank("pk not set", pk);
    Assert.notBlank("devId not set", pk);
  }

  public String getDownTopic() {
    assertValuesSet();
    return getTopicPrefix() + "down/dev/" + pk + "/" + devId;
  }
}
