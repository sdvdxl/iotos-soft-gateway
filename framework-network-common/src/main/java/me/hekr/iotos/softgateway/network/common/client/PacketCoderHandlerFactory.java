package me.hekr.iotos.softgateway.network.common.client;

import io.netty.channel.ChannelHandler;

/**
 * <p>PacketCoderHandlerFactory interface.</p>
 *
 * @version $Id: $Id
 */
public interface PacketCoderHandlerFactory {

  /**
   * 获取ChannelHandler
   *
   * @return ChannelHandler
   */
  ChannelHandler getPacketCoderHandler();
}
