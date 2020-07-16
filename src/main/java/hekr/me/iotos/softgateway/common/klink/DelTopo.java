package hekr.me.iotos.softgateway.common.klink;

import hekr.me.iotos.softgateway.common.enums.Action;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
