package me.hekr.iotos.softgateway.network.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;
import java.net.InetSocketAddress;
import java.util.List;
import me.hekr.iotos.softgateway.network.common.Packet;
import me.hekr.iotos.softgateway.network.common.PacketCoder;

/**
 * 编解码
 *
 * @author iotos
 */
@Sharable
class UdpCodecHandler<T> extends MessageToMessageCodec<DatagramPacket, Packet<T>> {
  private final PacketCoder<T> packetCoder;
  private final String host;
  private final int port;

  public UdpCodecHandler(PacketCoder<T> udCoder, String host, int port) {
    this.packetCoder = udCoder;
    this.host = host;
    this.port = port;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Packet<T> msg, List<Object> out) {
    byte[] bytes = packetCoder.encode(msg.getMessage());
    if (bytes != null) {
      DatagramPacket datagramPacket =
          new DatagramPacket(Unpooled.wrappedBuffer(bytes), new InetSocketAddress(host, port));
      out.add(datagramPacket);
    }
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
    ByteBuf buf = msg.content();
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);
    Object o = packetCoder.decode(bytes);
    if (o != null) {
      out.add(Packet.wrap(o, msg.sender()));
    }
  }
}
