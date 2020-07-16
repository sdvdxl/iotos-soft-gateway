package hekr.me.iot.softgateway.common.dto;

import lombok.Data;

@Data
public class EntranceReq {

  private String carCode;
  /** 进场时间 */
  private String inTime;
  /** 过场时间 */
  private String passTime;

  private String parkID;
  /** 0:进场，1：出场 */
  private String inOrOut;
  /** 车辆本次进场出场标识 */
  private String GUID;
  /** 通道id */
  private String channelID;
  /** 通道名称 */
  private String channelName;
  /** 图片路径地址 */
  private String imagePath;
}
