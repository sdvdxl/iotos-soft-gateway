package me.hekr.iotos.softgateway.network.common.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.MessageListener;
import me.hekr.iotos.softgateway.network.common.PacketContext;
import org.springframework.stereotype.Service;

/** @author iotos */
@Sharable
@Service
@Slf4j
public class ClientMessageHandler<T> extends SimpleChannelInboundHandler<InternalPacket<T>> {
  private static final AttributeKey<PacketContext<?>> PACKET_CONTEXT =
      AttributeKey.valueOf("_PACKET_CONTEXT_");
  private final AbstractClient<T> client;
  private final boolean sync;
  private final MessageListener<PacketContext<T>> messageListener;
  private final EventListener<T> eventListener;

  public ClientMessageHandler(
      AbstractClient<T> client,
      MessageListener<PacketContext<T>> messageListener,
      EventListener<T> eventListener,
      boolean sync) {
    this.client = client;
    this.messageListener = messageListener;
    this.eventListener = eventListener;
    this.sync = sync;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, InternalPacket<T> packet) {
    // 如果不是同步，调用消息回调接口
    if (!sync) {
      messageListener.onMessage(PacketContext.wrap(packet.getAddress(), packet.getMessage()));
      return;
    }

    // 同步不调用回调接口
    synchronized (client.LOCK) {
      client.result = packet.getMessage();
      client.signalAll();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    log.error("remote:" + ctx.channel().remoteAddress() + ",未处理的异常，" + cause.getMessage(), cause);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    PacketContext<T> packetContext =
        PacketContext.wrap((InetSocketAddress) ctx.channel().remoteAddress());
    ctx.channel().attr(PACKET_CONTEXT).set(packetContext);

    eventListener.onConnect(packetContext);
  }

  @SuppressWarnings("unchecked")
  private PacketContext<T> getPacketContext(ChannelHandlerContext ctx) {
    return (PacketContext<T>) ctx.channel().attr(PACKET_CONTEXT).get();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    try {
      PacketContext<T> packetContext = getPacketContext(ctx);
      eventListener.onDisconnect(packetContext, CloseReason.SERVER_CLOSED);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    ctx.channel().eventLoop().execute(client::loopConnect);
  }
}
