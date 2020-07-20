package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/20
 */
@Data
public class GateControlReq {
  private int channelID;
  private int controlType;
  private String reason;
  private String userID;
}
