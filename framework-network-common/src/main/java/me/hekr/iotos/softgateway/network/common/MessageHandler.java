package me.hekr.iotos.softgateway.network.common;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** @author iotos */
@Sharable
@Service
@Slf4j
public class MessageHandler<T> extends SimpleChannelInboundHandler<Packet<T>> {

  private final AbstractClient<T> client;
  private final boolean sync;
  private final MessageListener<T> messageListener;

  public MessageHandler(
      AbstractClient<T> client, MessageListener<T> messageListener, boolean sync) {
    this.client = client;
    this.messageListener = messageListener;
    this.sync = sync;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Packet<T> packet) throws Exception {
    // 如果不是同步，调用消息回调接口
    if (!sync) {
      messageListener.onMessage(packet.getAddress(), packet.getMessage());
      return;
    }

    // 同步不调用回调接口
    synchronized (client.LOCK) {
      client.result = packet.getMessage();
      client.LOCK.notifyAll();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("remote:" + ctx.channel().remoteAddress() + ",未处理的异常，" + cause.getMessage(), cause);
  }
}
