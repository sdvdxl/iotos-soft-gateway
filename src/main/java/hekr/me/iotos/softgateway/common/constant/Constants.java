package hekr.me.iotos.softgateway.common.constant;

public class Constants {
  /** 换行符 */
  private static String lineSeparator = System.getProperty("line.separator", "\n");

  public static final String NEXT_LINE = lineSeparator;

  /** hash加密方法 */
  public static final String HASH_METHOD = "HmacSHA1";
  /** 加密随机值 */
  public static final String RANDOM = "random";
}
