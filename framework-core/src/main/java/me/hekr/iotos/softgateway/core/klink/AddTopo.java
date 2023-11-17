package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
/**
 * <p>AddTopo class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Setter
@ToString(callSuper = true)
public class AddTopo extends KlinkDev {

  /** 子设备校验信息 */
  private TopoSub sub;

  /** {@inheritDoc} */
  @Override
  public String getAction() {
    return Action.ADD_TOPO.getAction();
  }
}
