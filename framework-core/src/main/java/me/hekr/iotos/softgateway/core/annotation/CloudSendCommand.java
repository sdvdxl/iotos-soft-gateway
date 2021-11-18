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
 * <p>cmd，type， gateway 是 与 的关系。
 *
 * <p>举例： <code>@CloudSendCommand(cmd={"cmdA","cmdB"}, type={"typeA", "typeB"})</code> 可以匹配设备类型为
 * typeA或者typeB的，下发命令是 cmdA 或者 cmdB的数据。
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Target(ElementType.TYPE)
public @interface CloudSendCommand {

  /**
   * cmd列表，可以指定多个。
   *
   * <p>cmd 列表是或的关系。
   */
  String[] cmd();

  /**
   * 是否是网关，true是网关设备
   *
   * @return true 网关，否则是子设备
   */
  boolean gateway() default false;

  /** 类型 或的关系 */
  String[] type() default {};
}
