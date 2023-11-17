package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>DelTopo class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DelTopo extends KlinkDev {

  /** 子设备 */
  private TopoSub sub;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.DEL_TOPO.getAction();
  }
}
