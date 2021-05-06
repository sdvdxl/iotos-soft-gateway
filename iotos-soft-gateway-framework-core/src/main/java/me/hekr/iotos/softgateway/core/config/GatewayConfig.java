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
  protected String clientId;

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
    return "down/dev/" + pk + "/" + devId;
  }
}
