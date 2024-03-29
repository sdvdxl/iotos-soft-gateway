package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

/**
 * cloudSendResp
 *
 * @author iotos
 * @version $Id: $Id
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CloudSendResp extends KlinkResp {

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.CLOUD_SEND_RESP.getAction();
  }

  /** {@inheritDoc} */
  @Override
  public void setNewMsgId() {
    // cloudSendResp 不需要自动生成，使用cloudSend 的msgId作为配对
  }
}
