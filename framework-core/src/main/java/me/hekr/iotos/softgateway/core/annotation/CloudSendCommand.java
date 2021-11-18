package me.hekr.iotos.softgateway.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * iot 下发指令注解
 *
 * <p>cmd， type param （如果配置了值的话）全部匹配才会走到对应的类
 *
 * <p>思路： bean加载的时候，注入所有的 SubsystemCommandService 的bean；消息来的时候，从这些bean里面扫描所有匹配的bean，匹配成功，则调用 void
 * handle(DeviceRemoteConfig deviceRemoteConfig, ModelData data); 方法处理
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Target(ElementType.TYPE)
public @interface CloudSendCommand {

  /** cmd列表，必填 */
  String[] cmd() default {};

  /**
   * 是否是网关，true是网关设备
   *
   * @return true 网关，否则是子设备
   */
  boolean gateway() default false;

  /** 类型，可空 */
  String[] type() default {};
}
