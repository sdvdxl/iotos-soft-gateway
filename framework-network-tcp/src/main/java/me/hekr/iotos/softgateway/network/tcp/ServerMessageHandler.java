package me.hekr.iotos.softgateway.network.tcp;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.PacketContext;

/** @author iotos */
@Sharable
@Slf4j
public class ServerMessageHandler<T> extends SimpleChannelInboundHandler<InternalPacket<T>> {
  private static final AttributeKey<PacketContext<?>> PACKET_CONTEXT =
      AttributeKey.valueOf("_PACKET_CONTEXT_");
  private final TcpMessageListener<T> messageListener;
  private final EventListener<T> eventListener;

  public ServerMessageHandler(
      TcpMessageListener<T> messageListener, EventListener<T> eventListener) {
    this.messageListener = messageListener;
    this.eventListener = eventListener;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    TcpServerPacketContext<T> packetContext =
        TcpServerPacketContext.wrap(ctx, (InetSocketAddress) ctx.channel().remoteAddress());
    ctx.channel().attr(PACKET_CONTEXT).set(packetContext);

    eventListener.onConnect(packetContext);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    TcpServerPacketContext<T> packetContext = getPacketContext(ctx);
    CloseReason closeReason = packetContext.getCloseReason();
    if (closeReason == null) {
      closeReason = CloseReason.CLIENT_CLOSE;
    }

    eventListener.onDisconnect(packetContext, closeReason);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
        TcpServerPacketContext<T> packetContext = getPacketContext(ctx);
        packetContext.increaseHeartbeatTimeoutCount();
        LocalDateTime lastOccurTime = packetContext.getOccurTime();
        packetContext.setOccurTime(LocalDateTime.now());
        eventListener.onHeartbeatTimeout(
            packetContext, lastOccurTime, packetContext.getHeartbeatTimeoutCount());
      }
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, InternalPacket<T> packet) {
    TcpServerPacketContext<T> packetContext = getPacketContext(ctx);
    packetContext.resetHeartbeatTimeoutCount();
    packetContext.setMessage(packet.getMessage());
    messageListener.onMessage(packetContext);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    eventListener.exceptionCaught(getPacketContext(ctx), cause);
  }

  @SuppressWarnings("unchecked")
  private TcpServerPacketContext<T> getPacketContext(ChannelHandlerContext ctx) {
    return (TcpServerPacketContext<T>) ctx.channel().attr(PACKET_CONTEXT).get();
  }
}
