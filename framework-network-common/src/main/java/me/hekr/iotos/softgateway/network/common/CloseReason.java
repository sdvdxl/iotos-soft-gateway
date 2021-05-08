package me.hekr.iotos.softgateway.network.common;

import lombok.Getter;

/** @author iotos */
public enum CloseReason {
  CLIENT_CLOSE("客户端主动关闭"),
  SERVER_CLOSED("服务端主动关闭"),
  HEARTBEAT_TIMEOUT("心跳超时关闭"),
  OTHER("未知原因");

  @Getter private final String reason;

  CloseReason(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return this.name() + "(" + reason + ")";
  }
}
