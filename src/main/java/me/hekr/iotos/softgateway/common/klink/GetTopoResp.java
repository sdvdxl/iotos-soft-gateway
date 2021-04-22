package me.hekr.iotos.softgateway.common.klink;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.common.enums.Action;

@Getter
@Setter
@ToString(callSuper = true)
public class GetTopoResp extends KlinkResp {

  private static final long serialVersionUID = -4617625211601039076L;
  private List<Dev> subs;

  @Override
  public String getAction() {
    return Action.GET_TOPO_RESP.getAction();
  }
}
