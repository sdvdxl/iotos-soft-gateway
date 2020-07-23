package hekr.me.iotos.softgateway.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EntranceReq {

  /** 必选，车牌码 */
  private String carCode;
  /** 必选，进场时间 */
  private String inTime;
  /** 必选，过场时间 */
  private String passTime;

  /** 必选，停车场id */
  private String parkID;
  /** 必选，0:进场，1：出场 */
  private String inOrOut;
  /** 必选，车辆本次进场出场标识 */
  @JsonProperty("GUID")
  private String guid;
  /** 必选，通道id */
  private String channelID;
  /** 通道名称 */
  private String channelName;
  /** 图片路径地址 */
  private String imagePath;
}
