package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import com.example.iotcloudgateway.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** @author du */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class AddTopoResp extends KlinkResp {

  /** 子设备 */
  private Dev sub;

  public AddTopoResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  public AddTopoResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.ADD_TOPO_RESP.getAction();
  }
}
