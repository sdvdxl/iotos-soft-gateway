package me.hekr.iotos.softgateway.core.constant;

public class Constants {
  /** 换行符 */
  private static final String lineSeparator = System.getProperty("line.separator", "\n");

  public static final String NEXT_LINE = lineSeparator;

  /** hash加密方法 */
  public static final String HASH_METHOD = "HmacSHA1";
  /** 加密随机值 */
  public static final String RANDOM = "random";

  public static final String NONCE = "nonce";
}
