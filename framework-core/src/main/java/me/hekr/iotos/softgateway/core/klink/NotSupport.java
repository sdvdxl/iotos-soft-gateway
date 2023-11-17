package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>NotSupport class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
public class NotSupport extends KlinkDev {
  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.NOT_SUPPORT.getAction();
  }
}
