package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DevLogin extends KlinkDev {

  private String random;
  private String hashMethod;
  private String sign;

  @Override
  public String getAction() {
    return Action.DEV_LOGIN.getAction();
  }
}
