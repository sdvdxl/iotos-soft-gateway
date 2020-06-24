package com.example.iotcloudgateway.server.tcp;

import org.tio.core.intf.Packet;

public class TcpPacket extends Packet {
  public static final int HEADER_LENGTH = 4; // 消息头的长度
  public static final int LEN_MAX = 2048; // 消息最长（防止socket字节流攻击）
  public static final String CHARSET = "utf-8";
  public static final byte[] SPLIT = {10};
  private byte[] body;

  /** @return the body */
  public byte[] getBody() {
    return body;
  }

  /** @param body the body to set */
  public void setBody(byte[] body) {
    this.body = body;
  }
}
