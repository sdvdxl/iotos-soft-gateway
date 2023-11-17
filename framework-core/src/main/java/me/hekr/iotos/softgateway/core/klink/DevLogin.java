package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>DevLogin class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
public class DevLogin extends KlinkDev {

  private String random;
  private String hashMethod;
  private String sign;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEV_LOGIN.getAction();
  }
}
