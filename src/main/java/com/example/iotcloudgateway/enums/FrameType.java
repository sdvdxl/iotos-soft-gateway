package com.example.iotcloudgateway.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;

/**
 * @author du
 *     <p>设备数据帧类型（从设备角度看数据流向）
 */
public enum FrameType {
  /** 设备上行 */
  DEV_UP("up"),

  /** 设备下行 */
  DEV_DOWN("down"),

  DEV_UP_DOWN("up_down"),

  /** 内部 */
  INNER("inner");

  private static final Map<String, FrameType> TYPE_MAP =
      Arrays.stream(FrameType.values()).collect(toMap(
          FrameType::getType, Function.identity()));
  @Getter private String type;

  FrameType(String type) {
    this.type = type;
  }

  public static FrameType of(String action) {
    return TYPE_MAP.get(action);
  }
}
