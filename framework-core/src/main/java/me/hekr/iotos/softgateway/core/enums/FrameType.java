package me.hekr.iotos.softgateway.core.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

/**
 * 设备数据帧类型（从设备角度看数据流向）
 *
 * @author du
 * @version $Id: $Id
 */
public enum FrameType {
  /** 设备上行 */
  DEV_UP("up"),

  /** 设备下行 */
  DEV_DOWN("down"),

  DEV_UP_DOWN("up_down"),

  /** 内部 */
  INNER("inner");

  /** Constant <code>TYPE_MAP</code> */
  private static final Map<String, FrameType> TYPE_MAP =
      Arrays.stream(FrameType.values()).collect(toMap(FrameType::getType, Function.identity()));
  @Getter private final String type;

  FrameType(String type) {
    this.type = type;
  }

  /**
   * <p>of.</p>
   *
   * @param action a {@link java.lang.String} object.
   * @return a {@link me.hekr.iotos.softgateway.core.enums.FrameType} object.
   */
  public static FrameType of(String action) {
    return TYPE_MAP.get(action);
  }
}
