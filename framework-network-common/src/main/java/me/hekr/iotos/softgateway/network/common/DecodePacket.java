package me.hekr.iotos.softgateway.network.common;

import lombok.Data;

/**
 * <p>DecodePacket class.</p>
 *
 * @version $Id: $Id
 */
@Data
public class DecodePacket {
  /** Constant <code>NULL</code> */
  public static final DecodePacket NULL = new DecodePacket();
  /** decode 结果 */
  private Object result;
  /** 数组读取大小，要真实填写 */
  private int readSize;

  /**
   * <p>wrap.</p>
   *
   * @param result a {@link java.lang.Object} object.
   * @param readSize a int.
   * @return a {@link me.hekr.iotos.softgateway.network.common.DecodePacket} object.
   */
  public static DecodePacket wrap(Object result, int readSize) {
    DecodePacket r = new DecodePacket();
    r.result = result;
    r.readSize = readSize;
    return r;
  }
}
