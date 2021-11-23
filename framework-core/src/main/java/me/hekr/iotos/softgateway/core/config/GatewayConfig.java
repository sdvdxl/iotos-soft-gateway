package me.hekr.iotos.softgateway.core.config;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

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

  /**
   * 传入的设备是否是网关自身
   *
   * @param pk 要判断的设备pk
   * @param devId 要判断的设备devId
   * @return pk，devId都为空返回true；否则判断网关和设备信息是否匹配，匹配返回true是网关
   */
  public boolean isGateway(String pk, String devId) {
    return StringUtils.isAllBlank(pk, devId) || (this.pk.equals(pk) && this.devId.equals(devId));
  }
}
