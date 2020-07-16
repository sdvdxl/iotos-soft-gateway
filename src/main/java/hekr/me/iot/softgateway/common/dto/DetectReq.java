package hekr.me.iot.softgateway.common.dto;

import lombok.Data;

@Data
public class DetectReq {

  /** 必选，车牌号码 */
  private String carCode;
  /** 必选，过场时间（格式yyyy-MM-dd HH:mm:ss）（进场时等于进场时间，出场时为出场时间） */
  private String passTime;
  /** 必选，车场ID */
  private String parkID;
  /** 必选，0:进场，1：出场 */
  private String inOrOut;
  /** 必选，通道ID */
  private String channelID;
  /** 通道名称 */
  private String channelName;
  /** 图片路径地址 */
  private String imagePath;
}
