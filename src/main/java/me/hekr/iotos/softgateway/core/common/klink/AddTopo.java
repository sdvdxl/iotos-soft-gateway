package me.hekr.iotos.softgateway.core.common.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.common.enums.Action;

@Getter
@Setter
@ToString(callSuper = true)
public class AddTopo extends KlinkDev {

  /** 子设备校验信息 */
  private TopoSub sub;

  @Override
  public String getAction() {
    return Action.ADD_TOPO.getAction();
  }
}
