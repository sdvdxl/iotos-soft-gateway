package me.hekr.iotos.softgateway.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author du
 * @date 2023/11/16 13:28
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CacheDeviceKey {
  private String pk;
  private String devId;
  String param;

  public static CacheDeviceKey of(String pk, String devId, String param) {
    return new CacheDeviceKey(pk, devId, param);
  }
}
