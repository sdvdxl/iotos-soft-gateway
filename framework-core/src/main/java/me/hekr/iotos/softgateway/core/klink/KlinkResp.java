package me.hekr.iotos.softgateway.core.klink;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;

/**
 * <p>KlinkResp class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@ToString(callSuper = true)
@Getter
@Setter
public class KlinkResp extends KlinkDev {

  private static final long serialVersionUID = -4341021820638489039L;
  protected int code;

  @JsonInclude(Include.NON_NULL)
  protected String desc;

  @JsonInclude(Include.NON_NULL)
  protected Map<String, Object> params;

  /**
   * <p>Constructor for KlinkResp.</p>
   */
  public KlinkResp() {
    this.code = ErrorCode.SUCCESS.getCode();
  }

  /**
   * 如果 不成功，desc 使用 errorCode 的描述
   *
   * @param errorCode 错误码
   */
  public KlinkResp(ErrorCode errorCode) {
    this(errorCode, null);
  }

  /**
   * <p>Constructor for KlinkResp.</p>
   *
   * @param errorCode 错误码
   * @param desc 1. 不为空，使用参数 desc；2. 为空：如果 errorCode 不成功，则使用 * errorCode的desc，否则保持desc为null
   * @param params 其他参数
   */
  public KlinkResp(ErrorCode errorCode, String desc, Map<String, Object> params) {
    this.code = errorCode.getCode();
    this.desc = errorCode.getDesc();
    if (desc != null) {
      this.desc += ", " + desc;
    }
    this.params = params;
  }

  /**
   * <p>Constructor for KlinkResp.</p>
   *
   * @param errorCode a {@link me.hekr.iotos.softgateway.core.enums.ErrorCode} object.
   * @param desc a {@link java.lang.String} object.
   */
  public KlinkResp(ErrorCode errorCode, String desc) {
    this(errorCode, desc, null);
  }

  /**
   * <p>isSuccess.</p>
   *
   * @see ErrorCode#SUCCESS
   * @return a boolean.
   */
  @JsonIgnore
  public boolean isSuccess() {
    return code == ErrorCode.SUCCESS.getCode();
  }

  /**
   * 设置错误码和默认错误描述
   *
   * <p>注意desc会被覆盖为 ErrorCode 的描述，如果不想覆盖则使用 setCode
   *
   * @param errorCode 错误码
   * @return this
   */
  public KlinkResp setErrorCode(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.desc = errorCode.getDesc();

    return this;
  }
}
