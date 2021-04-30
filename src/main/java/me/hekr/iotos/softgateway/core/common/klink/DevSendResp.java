package me.hekr.iotos.softgateway.core.common.klink;

import me.hekr.iotos.softgateway.core.common.enums.Action;

/**
 * 设备发送回复 <br>
 *
 * @author iotos <br>
 * @date 2020/7/21 16:08 <br>
 */
public class DevSendResp extends KlinkResp {

  @Override
  public String getAction() {
    return Action.DEV_SEND_RESP.getAction();
  }
}
