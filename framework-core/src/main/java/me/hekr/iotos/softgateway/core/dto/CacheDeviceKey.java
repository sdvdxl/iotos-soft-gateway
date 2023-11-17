package me.hekr.iotos.softgateway.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * CacheDeviceKey class.
 *
 * @author du
 * @version $Id: $Id
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"pk", "devId", "param"})
public class CacheDeviceKey {
  private String pk;
  private String devId;
  private String param;
  private String cmd;

  /**
   * Constructor for CacheDeviceKey.
   *
   * @param pk a {@link java.lang.String} object
   * @param devId a {@link java.lang.String} object
   * @param param a {@link java.lang.String} object
   */
  public CacheDeviceKey(String pk, String devId, String param) {
    this.pk = pk;
    this.devId = devId;
    this.param = param;
  }

  /**
   * of.
   *
   * @param pk a {@link java.lang.String} object
   * @param devId a {@link java.lang.String} object
   * @param param a {@link java.lang.String} object
   * @return a {@link me.hekr.iotos.softgateway.core.dto.CacheDeviceKey} object
   */
  public static CacheDeviceKey of(String pk, String devId, String param) {
    return new CacheDeviceKey(pk, devId, param);
  }

  /**
   * equalsDev.
   *
   * @param pk a {@link java.lang.String} object
   * @param devId a {@link java.lang.String} object
   * @return a boolean
   */
  public boolean equalsDev(String pk, String devId) {
    return this.pk.equals(pk) && this.devId.equals(devId);
  }
}
