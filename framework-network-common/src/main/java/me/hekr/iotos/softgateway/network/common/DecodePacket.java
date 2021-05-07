package me.hekr.iotos.softgateway.network.common;

import lombok.Data;

/** @author iotos */
@Data
public class DecodePacket {
  public static final DecodePacket NULL = new DecodePacket();
  /** decode 结果 */
  private Object result;
  /** 数组读取大小，要真实填写 */
  private int readSize;

  public static DecodePacket wrap(Object result, int readSize) {
    DecodePacket r = new DecodePacket();
    r.result = result;
    r.readSize = readSize;
    return r;
  }
}
