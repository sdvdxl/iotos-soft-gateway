package me.hekr.iotos.softgateway.common.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.common.enums.Action;

@Getter
@Setter
@ToString(callSuper = true)
public class NotSupport extends KlinkDev {
  @Override
  public String getAction() {
    return Action.NOT_SUPPORT.getAction();
  }
}
