package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

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
