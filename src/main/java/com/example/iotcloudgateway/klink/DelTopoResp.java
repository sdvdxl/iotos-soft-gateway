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
public class DelTopoResp extends KlinkResp {

  /** 子设备 */
  private Dev sub;

  public DelTopoResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  public DelTopoResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.DEL_TOPO_RESP.getAction();
  }
}
