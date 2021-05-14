package me.hekr.iotos.softgateway.core.klink;

import me.hekr.iotos.softgateway.core.enums.Action;

/**
 * 设备发送回复 <br>
 *
 * @author iotos <br>
 */
public class DevSendResp extends KlinkResp {

  @Override
  public String getAction() {
    return Action.DEV_SEND_RESP.getAction();
  }
}
