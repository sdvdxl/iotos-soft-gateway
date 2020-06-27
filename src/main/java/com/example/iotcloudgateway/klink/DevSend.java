package com.example.iotcloudgateway.klink;

import com.example.iotcloudgateway.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** @author du */
@Getter
@Setter
@ToString(callSuper = true)
public class DevSend extends KlinkDev {

  private ModelData data;

  @Override
  public String getAction() {
    return Action.DEV_SEND.getAction();
  }
}
