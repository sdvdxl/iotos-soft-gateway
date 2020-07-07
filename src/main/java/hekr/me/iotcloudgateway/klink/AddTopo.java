package hekr.me.iotcloudgateway.klink;

import hekr.me.iotcloudgateway.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
