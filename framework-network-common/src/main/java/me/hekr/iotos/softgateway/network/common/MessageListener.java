package me.hekr.iotos.softgateway.network.common;

import java.net.SocketAddress;

/** @author iotos */
public interface MessageListener<T> {

  /**
   * 收到消息
   *
   * @param addr 地址
   * @param msg 消息
   */
  void onMessage(SocketAddress addr, T msg);
}
