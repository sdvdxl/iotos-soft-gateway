package me.hekr.iotos.softgateway.network.common.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.MessageListener;
import me.hekr.iotos.softgateway.network.common.PacketContext;
import org.springframework.stereotype.Service;

/** @author iotos */
@Sharable
@Service
@Slf4j
public class ClientMessageHandler<T> extends SimpleChannelInboundHandler<InternalPacket<T>> {

  private final AbstractClient<T> client;
  private final boolean sync;
  private final MessageListener<T> messageListener;

  public ClientMessageHandler(
      AbstractClient<T> client, MessageListener<T> messageListener, boolean sync) {
    this.client = client;
    this.messageListener = messageListener;
    this.sync = sync;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, InternalPacket<T> packet) {
    // 如果不是同步，调用消息回调接口
    if (!sync) {
      messageListener.onMessage(PacketContext.wrap(ctx, packet.getAddress(), packet.getMessage()));
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
}
