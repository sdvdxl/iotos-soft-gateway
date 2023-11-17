package me.hekr.iotos.softgateway.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>SpringContextUtils class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

  /** 上下文对象实例 */
  private static ApplicationContext applicationContext;

  /** {@inheritDoc} */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtils.applicationContext = applicationContext;
  }

  /**
   * 获取applicationContext
   *
   * @return ApplicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 通过class获取Bean.
   *
   * @param clazz 类
   * @return bean
   * @param <T> a T object.
   */
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   *
   * @param name bean name
   * @param clazz 类
   * @return bean
   * @param <T> a T object.
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }
}
