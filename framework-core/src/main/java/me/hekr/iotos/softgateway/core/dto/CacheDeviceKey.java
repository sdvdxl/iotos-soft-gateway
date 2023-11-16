package me.hekr.iotos.softgateway.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author du
 * @date 2023/11/16 13:28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"pk", "devId", "param"})
public class CacheDeviceKey {
  private String pk;
  private String devId;
  private String param;
  private String cmd;

  public CacheDeviceKey(String pk, String devId, String param) {
    this.pk = pk;
    this.devId = devId;
    this.param = param;
  }

  public static CacheDeviceKey of(String pk, String devId, String param) {
    return new CacheDeviceKey(pk, devId, param);
  }

  public boolean equalsDev(String pk, String devId) {
    return this.pk.equals(pk) && this.devId.equals(devId);
  }
}
