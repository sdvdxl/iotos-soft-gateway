package me.hekr.iotos.softgateway.core.constant;

public class Constants {
  /** 加密随机值 */
  public static final String RANDOM = "random";
  /** 下个版本删除 */
  @Deprecated public static final String CMD_BEAN_SUFFIX = "@SubSystemCommandService";
  /** 换行符 */
  private static final String lineSeparator = System.getProperty("line.separator", "\n");

  public static final String NEXT_LINE = lineSeparator;
}
