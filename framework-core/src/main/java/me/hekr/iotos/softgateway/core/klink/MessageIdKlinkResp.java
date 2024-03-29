package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>MessageIdKlinkResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@ToString(callSuper = true)
@Getter
@Setter
public class MessageIdKlinkResp extends KlinkResp {

  private static final long serialVersionUID = 6100726877614668403L;
  protected String messageId;
}
