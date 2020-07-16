package hekr.me.iot.softgateway.common.dto;

import lombok.Data;

@Data
public class DetectResp {

  /** 必选，返回值，0为成功，其他为失败 */
  private int resCode;
  /** 返回信息 */
  private String resMsg;
}
