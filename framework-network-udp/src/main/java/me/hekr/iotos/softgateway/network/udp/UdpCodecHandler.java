package me.hekr.iotos.softgateway.network.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;
import java.util.List;
import me.hekr.iotos.softgateway.network.common.DecodePacket;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;

/**
 * 编解码
 *
 * @author iotos
 */
class UdpCodecHandler<T> extends MessageToMessageCodec<DatagramPacket, InternalPacket<T>> {
  private final PacketCoder<T> packetCoder;

  /**
   * <p>Constructor for UdpCodecHandler.</p>
   *
   * @param udCoder a {@link me.hekr.iotos.softgateway.network.common.coder.PacketCoder} object.
   */
  public UdpCodecHandler(PacketCoder<T> udCoder) {
    this.packetCoder = udCoder;
  }

  /** {@inheritDoc} */
  @Override
  protected void encode(ChannelHandlerContext ctx, InternalPacket<T> msg, List<Object> out) {
    byte[] bytes = packetCoder.encode(msg.getMessage());

    if (bytes != null) {
      DatagramPacket datagramPacket =
          new DatagramPacket(Unpooled.wrappedBuffer(bytes), msg.getAddress());
      out.add(datagramPacket);
    }
  }

  /** {@inheritDoc} */
  @Override
  protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
    ByteBuf buf = msg.content();
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);
    DecodePacket o = packetCoder.decode(bytes);
    if (o != null) {
      out.add(InternalPacket.wrap(o.getResult(), msg.sender()));
    }
  }
}
