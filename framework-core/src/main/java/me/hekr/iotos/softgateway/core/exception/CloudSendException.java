package me.hekr.iotos.softgateway.core.exception;

import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import me.hekr.iotos.softgateway.core.klink.ModelData;

/**
 * CloudSendException
 *
 * @author iotos
 */
public class CloudSendException extends RuntimeException {

  public CloudSendException(String pk, String devId, ModelData modelData, String desc) {
    super(desc + ", pk:" + pk + ", devId:" + devId + ", modelData:" + JsonUtil.toJson(modelData));
  }
}
