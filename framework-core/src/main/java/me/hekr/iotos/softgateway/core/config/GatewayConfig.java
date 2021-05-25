package me.hekr.iotos.softgateway.core.config;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.ToString;

/** @author iotos */
@Getter
@ToString
public class GatewayConfig {
  protected String pk;
  protected String devId;
  protected String devSecret;

  String getPublishTopic() {
    assertValuesSet();
    return "up/dev/" + pk + "/" + devId;
  }

  private void assertValuesSet() {
    Assert.notBlank("pk not set", pk);
    Assert.notBlank("devId not set", pk);
  }

  String getSubscribeTopic() {
    assertValuesSet();
    return "down/dev/" + pk + "/" + devId;
  }
}
