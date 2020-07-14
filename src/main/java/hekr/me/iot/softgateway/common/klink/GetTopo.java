package hekr.me.iot.softgateway.common.klink;

import hekr.me.iot.softgateway.common.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class GetTopo extends KlinkDev {

  private static final long serialVersionUID = 4118414118485680261L;

  @Override
  public String getAction() {
    return Action.GET_TOPO.getAction();
  }
}
