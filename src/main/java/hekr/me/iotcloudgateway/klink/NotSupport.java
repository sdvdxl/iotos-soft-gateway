package hekr.me.iotcloudgateway.klink;

import hekr.me.iotcloudgateway.enums.Action;
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
