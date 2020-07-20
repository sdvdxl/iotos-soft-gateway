package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

@Data
public class BaseResp {
  /** 必选，返回值，0为成功，其他为失败 */
  private int resCode;
  /** 返回信息 */
  private String resMsg;

  public BaseResp() {};

  public BaseResp(int resCode, String resMsg) {
    this.resCode = resCode;
    this.resMsg = resMsg;
  }
}
