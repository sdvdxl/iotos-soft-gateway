package me.hekr.iotos.softgateway.core.exception;

/** 远程配置不合法 */
public class IllegalRemoteConfigException extends RuntimeException {

  public IllegalRemoteConfigException(String message) {
    super(message);
  }
}
