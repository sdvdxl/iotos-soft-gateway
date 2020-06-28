package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import com.example.iotcloudgateway.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class DevLoginResp extends KlinkResp {

  private String random;
  private String hashMethod;
  private String sign;

  public DevLoginResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  public DevLoginResp(ErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public String getAction() {
    return Action.DEV_LOGIN_RESP.getAction();
  }
}
