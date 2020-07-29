package hekr.me.iotos.softgateway.common.dto;

import lombok.Data;

/**
 * @author jiatao
 * @date 2020/7/29
 */
@Data
public class Area {
  /** 区域Id */
  private String Id;
  /** 区域名称 */
  private String Name;
  /** 区域上级Id */
  private String ParentId;
}
