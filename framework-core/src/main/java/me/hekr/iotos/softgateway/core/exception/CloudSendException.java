package me.hekr.iotos.softgateway.core.exception;

import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.klink.ModelData;

/**
 * CloudSendException
 *
 * @author iotos
 * @version $Id: $Id
 */
public class CloudSendException extends RuntimeException {

  /**
   * <p>Constructor for CloudSendException.</p>
   *
   * @param pk a {@link java.lang.String} object.
   * @param devId a {@link java.lang.String} object.
   * @param modelData a {@link me.hekr.iotos.softgateway.core.klink.ModelData} object.
   * @param desc a {@link java.lang.String} object.
   */
  public CloudSendException(String pk, String devId, ModelData modelData, String desc) {
    super(desc + ", pk:" + pk + ", devId:" + devId + ", modelData:" + JsonUtil.toJson(modelData));
  }
}
