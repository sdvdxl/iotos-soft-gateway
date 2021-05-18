package me.hekr.iotos.softgateway.network.common.listener;

import me.hekr.iotos.softgateway.network.common.PacketContext;

/** @author iotos */
public interface MessageListener<T extends PacketContext<?>> {

  /**
   * 收到消息
   *
   * @param ctx 上下文
   */
  void onMessage(T ctx);
}
