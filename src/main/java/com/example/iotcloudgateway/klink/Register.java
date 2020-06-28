package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Register extends KlinkDev {

  private static final long serialVersionUID = 6099206158766831129L;
  private String random;
  private String hashMethod;
  private String sign;
  private String name;
  private String imei;
  private String batchName;

  @Override
  public String getAction() {
    return Action.REGISTER.getAction();
  }
}
