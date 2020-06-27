package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** @author du */
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
