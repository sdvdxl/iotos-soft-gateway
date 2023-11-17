package me.hekr.iotos.softgateway.core.klink;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hekr.iotos.softgateway.core.enums.Action;

/**
 * <p>DevLogout class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DevLogout extends KlinkDev {

  /** 登出原因 */
  private String reason;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEV_LOGOUT.getAction();
  }
}
