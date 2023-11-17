package me.hekr.iotos.softgateway.network.common.listener;

import me.hekr.iotos.softgateway.network.common.ConnectionContext;

/**
 * <p>MessageListener interface.</p>
 *
 * @version $Id: $Id
 */
public interface MessageListener<T extends ConnectionContext<?>> {

  /**
   * 收到消息
   *
   * @param ctx 上下文
   */
  void onMessage(T ctx);
}
