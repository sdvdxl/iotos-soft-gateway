package me.hekr.iotos.softgateway.common.utils;

/**
 * <p>NumberUtil class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
public class NumberUtil extends cn.hutool.core.util.NumberUtil {
  /**
   * <p>getOrDefault0.</p>
   *
   * @param value a {@link java.lang.Double} object
   * @return a {@link java.lang.Double} object
   */
  public static Double getOrDefault0(Double value) {
    if (value == null || value.isNaN() || value.isInfinite()) {
      return 0D;
    }
    return value;
  }

  /**
   * <p>getOrDefault0.</p>
   *
   * @param value a {@link java.lang.Integer} object
   * @return a {@link java.lang.Integer} object
   */
  public static Integer getOrDefault0(Integer value) {
    if (value == null) {
      return 0;
    }
    return value;
  }

  /**
   * <p>getOrDefault0.</p>
   *
   * @param value a {@link java.lang.Long} object
   * @return a {@link java.lang.Long} object
   */
  public static Long getOrDefault0(Long value) {
    if (value == null) {
      return 0L;
    }
    return value;
  }
}
