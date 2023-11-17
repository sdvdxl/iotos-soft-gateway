package me.hekr.iotos.softgateway.core.dto;

import cn.hutool.http.HttpStatus;
import java.util.HashMap;

/**
 * 操作消息提醒
 *
 * @author ruoyi
 * @version $Id: $Id
 */
public class AjaxResult<T> extends HashMap<String, Object> {
  /** 状态码 */
  public static final String CODE_TAG = "code";
  /** 返回内容 */
  public static final String MSG_TAG = "message";
  /** 数据对象 */
  public static final String DATA_TAG = "data";

  private static final long serialVersionUID = 1L;

  /**
   * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
   */
  public AjaxResult() {}

  /**
   * 初始化一个新创建的 AjaxResult 对象
   *
   * @param code 状态码
   * @param msg 返回内容
   */
  public AjaxResult(int code, String msg) {
    super.put(CODE_TAG, code);
    super.put(MSG_TAG, msg);
  }

  /**
   * 初始化一个新创建的 AjaxResult 对象
   *
   * @param code 状态码
   * @param msg 返回内容
   * @param data 数据对象
   */
  public AjaxResult(int code, String msg, T data) {
    super.put(CODE_TAG, code);
    super.put(MSG_TAG, msg);
    if (data != null) {
      super.put(DATA_TAG, data);
    }
  }

  /**
   * 返回成功消息
   *
   * @return 成功消息
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> success() {
    return AjaxResult.success("操作成功");
  }

  /**
   * 返回成功数据
   *
   * @return 成功消息
   * @param data a T object
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> success(T data) {
    return AjaxResult.success("操作成功", data);
  }

  /**
   * 返回成功消息
   *
   * @param msg 返回内容
   * @return 成功消息
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> success(String msg) {
    return AjaxResult.success(msg, null);
  }

  /**
   * 返回成功消息
   *
   * @param msg 返回内容
   * @param data 数据对象
   * @return 成功消息
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> success(String msg, T data) {
    return new AjaxResult<>(0, msg, data);
  }

  /**
   * 返回错误消息
   *
   * @param <T> a T class
   * @return a {@link me.hekr.iotos.softgateway.core.dto.AjaxResult} object
   */
  public static <T> AjaxResult<T> error() {
    return AjaxResult.error("操作失败");
  }

  /**
   * 返回错误消息
   *
   * @param msg 返回内容
   * @return 警告消息
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> error(String msg) {
    return AjaxResult.error(msg, null);
  }

  /**
   * 返回错误消息
   *
   * @param msg 返回内容
   * @param data 数据对象
   * @return 警告消息
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> error(String msg, T data) {
    return new AjaxResult<>(HttpStatus.HTTP_BAD_REQUEST, msg, data);
  }

  /**
   * 返回错误消息
   *
   * @param code 状态码
   * @param msg 返回内容
   * @return 警告消息
   * @param <T> a T class
   */
  public static <T> AjaxResult<T> error(int code, String msg) {
    return new AjaxResult<>(code, msg, null);
  }
}
