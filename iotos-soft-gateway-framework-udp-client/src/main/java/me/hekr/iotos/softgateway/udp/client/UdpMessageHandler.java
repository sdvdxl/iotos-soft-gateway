package me.hekr.iotos.softgateway.udp.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** @author iotos */
@Sharable
@Service
@Slf4j
class UdpMessageHandler<T> extends SimpleChannelInboundHandler<Message<T>> {

  private final UdpClient<T> client;
  private final boolean sync;
  private final UdpMessageListener<T> messageListener;

  public UdpMessageHandler(
      UdpClient<T> client, UdpMessageListener<T> messageListener, boolean sync) {
    this.client = client;
    this.messageListener = messageListener;
    this.sync = sync;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message<T> msg) throws Exception {
    // 如果不是同步，调用消息回调接口
    if (!sync) {
      messageListener.onMessage(msg.addr, msg.body);
      return;
    }

    // 同步不调用回调接口
    synchronized (client.LOCK) {
      client.result = msg.body;
      client.LOCK.notifyAll();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("remote:" + ctx.channel().remoteAddress() + ",未处理的异常，" + cause.getMessage(), cause);
  }
}
