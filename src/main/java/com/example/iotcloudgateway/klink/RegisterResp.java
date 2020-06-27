package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import com.example.iotcloudgateway.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** @author du */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class RegisterResp extends KlinkResp {

  private static final long serialVersionUID = -6728983939835762139L;
  private String devSecret;

  public RegisterResp(ErrorCode errorCode, String desc) {
    super(errorCode, desc);
  }

  @Override
  public String getAction() {
    return Action.REGISTER_RESP.getAction();
  }
}
