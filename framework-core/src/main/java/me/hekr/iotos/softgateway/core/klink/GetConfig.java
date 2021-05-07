package me.hekr.iotos.softgateway.core.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.Action;

@Getter
@Setter
@ToString(callSuper = true)
public class GetConfig extends KlinkDev {
  @Override
  public String getAction() {
    return Action.GET_CONFIG.getAction();
  }
}
