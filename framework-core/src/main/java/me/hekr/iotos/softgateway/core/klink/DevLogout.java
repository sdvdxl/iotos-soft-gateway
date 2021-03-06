package me.hekr.iotos.softgateway.core.klink;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hekr.iotos.softgateway.core.enums.Action;

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
