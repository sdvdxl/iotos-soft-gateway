package me.hekr.iotos.softgateway.network.common.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.common.ConnectionContext;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.listener.MessageListener;

/**
 * <p>ClientMessageHandler class.</p>
 *
 * @author iotos
 * @version $Id: $Id
 */
@Sharable
@Slf4j
public class ClientMessageHandler<T> extends SimpleChannelInboundHandler<InternalPacket<T>> {
  private static final AttributeKey<ConnectionContext<?>> PACKET_CONTEXT =
      AttributeKey.valueOf("_PACKET_CONTEXT_");
  private final AbstractClient<T> client;
  private final boolean sync;
  private final MessageListener<ConnectionContext<T>> messageListener;
  private final EventListener<T> eventListener;

  /**
   * <p>Constructor for ClientMessageHandler.</p>
   *
   * @param client a {@link me.hekr.iotos.softgateway.network.common.client.AbstractClient} object.
   * @param messageListener a {@link me.hekr.iotos.softgateway.network.common.client.CommonMessageListener} object.
   * @param eventListener a {@link me.hekr.iotos.softgateway.network.common.client.EventListener} object.
   * @param sync a boolean.
   */
  public ClientMessageHandler(
      AbstractClient<T> client,
      CommonMessageListener<T> messageListener,
      EventListener<T> eventListener,
      boolean sync) {
    this.client = client;
    this.messageListener = messageListener;
    this.eventListener = eventListener;
    this.sync = sync;
  }

  /** {@inheritDoc} */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, InternalPacket<T> packet) {
    if (log.isDebugEnabled()) {
      log.debug("收到消息：{}", packet);
    }

    // 如果不是同步，调用消息回调接口
    if (!sync) {
      messageListener.onMessage(ConnectionContext.wrap(packet.getAddress(), packet.getMessage()));
      return;
    }

    // 同步不调用回调接口
    synchronized (client.LOCK) {
      client.result = packet.getMessage();
      client.signalAll();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    log.error("remote:" + ctx.channel().remoteAddress() + ",未处理的异常，" + cause.getMessage(), cause);
  }

  /** {@inheritDoc} */
  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ConnectionContext<T> connectionContext =
        ConnectionContext.wrap((InetSocketAddress) ctx.channel().remoteAddress());
    connectionContext.setConnectTime();
    ctx.channel().attr(PACKET_CONTEXT).set(connectionContext);

    eventListener.onConnect(connectionContext);
  }

  @SuppressWarnings("unchecked")
  private ConnectionContext<T> getPacketContext(ChannelHandlerContext ctx) {
    return (ConnectionContext<T>) ctx.channel().attr(PACKET_CONTEXT).get();
  }

  /** {@inheritDoc} */
  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      ConnectionContext<T> packetContext = getPacketContext(ctx);
      eventListener.onHeartbeatTimeout(packetContext);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    try {
      ConnectionContext<T> connectionContext = getPacketContext(ctx);
      eventListener.onDisconnect(connectionContext, CloseReason.SERVER_CLOSED);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    log.warn("链接断开，" + TimeUnit.MILLISECONDS.toSeconds(client.getReconnectWait()) + "s后重试");
    ctx.channel()
        .eventLoop()
        .schedule(client::loopConnect, client.getReconnectWait(), TimeUnit.MILLISECONDS);
  }
}
