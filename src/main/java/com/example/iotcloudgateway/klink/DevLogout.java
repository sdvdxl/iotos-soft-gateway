package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevLogout extends KlinkDev {

  /** 登出原因 */
  private String reason;

  @Override
  public String getAction() {
    return Action.DEV_LOGOUT.getAction();
  }
}