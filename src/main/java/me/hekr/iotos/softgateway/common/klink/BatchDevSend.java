package me.hekr.iotos.softgateway.common.klink;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.common.enums.Action;

/** @author du */
@Getter
@Setter
@ToString(callSuper = true)
public class BatchDevSend extends KlinkDev {

  private List<SuModelData> data;

  @Override
  public String getAction() {
    return Action.BATCH_DEV_SEND.getAction();
  }
}
