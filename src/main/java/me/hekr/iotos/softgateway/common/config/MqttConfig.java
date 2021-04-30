package me.hekr.iotos.softgateway.common.config;

import lombok.Getter;
import lombok.ToString;

/** @author iotos */
@ToString
@Getter
public class MqttConfig {
  protected String endpoint;
  protected String username;
  protected char[] password;
  protected String clientId;
  /** 连接超时时间，单位秒 */
  protected int connectTimeout;
  /** 心跳时间，单位秒 */
  protected int keepAliveTime;
}
