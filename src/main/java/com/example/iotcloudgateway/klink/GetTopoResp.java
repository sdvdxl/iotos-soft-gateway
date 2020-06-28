package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
