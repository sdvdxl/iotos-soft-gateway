package hekr.me.iotos.softgateway.common.klink;

import hekr.me.iotos.softgateway.common.enums.Action;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class NotSupport extends KlinkDev {
  @Override
  public String getAction() {
    return Action.NOT_SUPPORT.getAction();
  }
}
