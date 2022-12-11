package me.hekr.iotos.softgateway.common.utils;

/**
 * @author du
 */
public class NumberUtil extends cn.hutool.core.util.NumberUtil {
  public static Double getOrDefault0(Double value) {
    if (value == null || value.isNaN() || value.isInfinite()) {
      return 0D;
    }
    return value;
  }

  public static Integer getOrDefault0(Integer value) {
    if (value == null) {
      return 0;
    }
    return value;
  }

  public static Long getOrDefault0(Long value) {
    if (value == null) {
      return 0L;
    }
    return value;
  }
}
