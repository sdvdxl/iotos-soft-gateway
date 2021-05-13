package me.hekr.iotos.softgateway.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import java.net.InetSocketAddress;
import java.util.List;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;

/**
 * 编解码
 *
 * @author iotos
 */
@Sharable
class TcpCodecHandler<T> extends MessageToMessageCodec<ByteBuf, InternalPacket<T>> {
  private final PacketCoder<T> packetCoder;

  public TcpCodecHandler(PacketCoder<T> packetCoder) {
    this.packetCoder = packetCoder;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, InternalPacket<T> msg, List<Object> out) {
    byte[] bytes = packetCoder.encode(msg.getMessage());
    if (bytes != null) {
      out.add(Unpooled.wrappedBuffer(bytes));
    }
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
    byte[] bytes = ByteBufUtil.getBytes(buf);
    DecodePacket p = packetCoder.decode(bytes);
    if (p != null && p != DecodePacket.NULL) {
      buf.readBytes(p.getReadSize());
      out.add(
          InternalPacket.wrap(p.getResult(), (InetSocketAddress) ctx.channel().remoteAddress()));
    }
  }
}
