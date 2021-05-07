package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

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
