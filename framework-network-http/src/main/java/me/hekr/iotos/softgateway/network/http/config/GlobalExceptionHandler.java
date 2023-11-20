package me.hekr.iotos.softgateway.network.http.config;

import javax.servlet.http.HttpServletRequest;
import me.hekr.iotos.softgateway.core.dto.AjaxResult;
import me.hekr.iotos.softgateway.core.exception.BizException;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  @Autowired private Environment env;

  /** 权限校验异常 */

  /** 请求方式不支持 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public AjaxResult handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
    return AjaxResult.error(405, "不支持请求方法: " + e.getMethod());
  }

  /** 系统异常 */
  @ExceptionHandler(Exception.class)
  public AjaxResult handleException(Exception e, HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    log.error("请求地址':" + requestURI + " ,发生系统异常. " + e.getMessage(), e);
    return AjaxResult.error(500, "系统错误");
  }

  /** 系统异常 */
  @ExceptionHandler(BizException.class)
  public AjaxResult handleException(BizException e, HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    log.warn("请求地址':" + requestURI + " ,业务异常. " + e.getMessage(), e);
    return AjaxResult.error(400, e.getMessage());
  }

  /** 自定义验证异常 */
  @ExceptionHandler(BindException.class)
  public AjaxResult handleBindException(BindException e) {
    log.warn(e.getMessage(), e);
    String message = e.getAllErrors().get(0).getDefaultMessage();
    return AjaxResult.error(message);
  }

  /** 自定义验证异常 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    String message = e.getBindingResult().getFieldError().getDefaultMessage();
    return AjaxResult.error(message);
  }

  @ExceptionHandler(ClientAbortException.class)
  public AjaxResult handleClientAbortException(ClientAbortException e, HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    log.warn("请求地址':" + requestURI + " ,客户端终止请求. " + e.getMessage(), e);
    return AjaxResult.error("客户端终止请求");
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public AjaxResult handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e, HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    log.warn("请求地址':" + requestURI + " ,参数绑定错误. " + e.getMessage(), e);
    return AjaxResult.error("参数错误：" + e.getParameterName() + " 类型：" + e.getParameterType());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public AjaxResult handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException e, HttpServletRequest request) {
    String limit = env.getProperty("spring.servlet.multipart.max-file-size");
    String requestURI = request.getRequestURI();
    log.warn("请求地址':" + requestURI + " ,上传文件超过限制  " + limit + " ," + e.getMessage(), e);
    return AjaxResult.error("上传文件超过限制 " + limit);
  }
}
