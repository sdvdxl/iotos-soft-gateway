package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

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
