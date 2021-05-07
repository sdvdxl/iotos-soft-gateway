package me.hekr.iotos.softgateway.network.udp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * 编解码
 *
 * @author du
 */
class Codec<T> extends MessageToMessageCodec<DatagramPacket, Message<T>> {
  String host;
  int port;
  private final UdpCoder<T> udCoder;

  public Codec(UdpCoder<T> udCoder) {
    this.udCoder = udCoder;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Message<T> msg, List<Object> out)
      throws Exception {
    byte[] bytes = udCoder.encode(msg.getBody());
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
    Object o = udCoder.decode(bytes);
    if (o != null) {
      out.add(Message.wrap(o, msg.sender()));
    }
  }
}
