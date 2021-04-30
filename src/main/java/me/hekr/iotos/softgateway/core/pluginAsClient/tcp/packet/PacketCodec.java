package me.hekr.iotos.softgateway.core.pluginAsClient.tcp.packet;

import java.nio.ByteBuffer;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

/** 拆包组包抽象接口 */
public interface PacketCodec {

  /** 解码 */
  TcpPacket decode(
      ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
      throws AioDecodeException;

  /** 编码 */
  ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext);
}
