package me.hekr.iotos.softgateway.core.common.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
@Setter
public class MessageIdKlinkResp extends KlinkResp {

  private static final long serialVersionUID = 6100726877614668403L;
  protected String messageId;
}
