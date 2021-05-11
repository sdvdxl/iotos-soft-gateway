package me.hekr.iotos.softgateway.network.common;

/** @author iotos */
public interface MessageListener<T extends PacketContext<?>> {

  /**
   * 收到消息
   *
   * @param ctx
   */
  void onMessage(T ctx);
}
