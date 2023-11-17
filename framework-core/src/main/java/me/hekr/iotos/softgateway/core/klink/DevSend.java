package me.hekr.iotos.softgateway.core.klink;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>DevSend class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DevSend extends KlinkDev {

  private ModelData data;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEV_SEND.getAction();
  }
}
