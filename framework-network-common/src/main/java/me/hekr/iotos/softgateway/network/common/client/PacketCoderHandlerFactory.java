package me.hekr.iotos.softgateway.network.common.client;

import io.netty.channel.ChannelHandler;

/** @author du */
public interface PacketCoderHandlerFactory {

  /**
   * 获取ChannelHandler
   *
   * @return ChannelHandler
   */
  ChannelHandler getPacketCoderHandler();
}
