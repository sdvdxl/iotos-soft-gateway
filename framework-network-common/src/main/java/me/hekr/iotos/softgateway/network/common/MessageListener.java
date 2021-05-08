package me.hekr.iotos.softgateway.network.common;

/** @author iotos */
public interface MessageListener<T> {

  /**
   * 收到消息
   *
   * @param ctx
   */
  void onMessage(PacketContext<T> ctx);
}
