package hekr.me.iotcloudgateway.common.klink;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DevSend extends KlinkDev {

  private ModelData data;

  //  @Override
  //  public String getAction() {
  //    return Action.DEV_SEND.getAction();
  //  }
}
