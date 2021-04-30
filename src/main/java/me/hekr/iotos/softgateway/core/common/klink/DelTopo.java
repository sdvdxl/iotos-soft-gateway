package me.hekr.iotos.softgateway.core.common.klink;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.common.enums.Action;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DelTopo extends KlinkDev {

  /** 子设备 */
  private TopoSub sub;

  @Override
  public String getAction() {
    return Action.DEL_TOPO.getAction();
  }
}
