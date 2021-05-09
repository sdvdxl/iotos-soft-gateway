package me.hekr.iotos.softgateway.network.tcp;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.PacketCoder;
import me.hekr.iotos.softgateway.network.common.util.NetUtil;

/**
 * tcp 服务端
 *
 * @author iotos
 */
@Slf4j
public class TcpServer<T> {

  private ServerMessageHandler<T> messageHandler;
  private EventLoopGroup boss;
  private EventLoopGroup work;
  private int port;
  private TcpCodecHandler<T> tcpCodecHandler;
  private EventListener<T> eventListener;
  private TcpMessageListener<T> messageListener;
  private int timeout;

  public TcpServer() {
    Class<? extends Channel> channelClass = NioServerSocketChannel.class;
  }

  public void setMessageListener(TcpMessageListener<T> messageListener) {
    Objects.requireNonNull(eventListener, "messageListener 不能为 null");
    this.messageListener = messageListener;
  }

  public void setPackageCoder(PacketCoder<T> packetCoder) {
    this.tcpCodecHandler = new TcpCodecHandler<>(packetCoder);
  }

  public void setEventListener(EventListener<T> eventListener) {
    Objects.requireNonNull(eventListener, "eventListener 不能为 null");
    this.eventListener = eventListener;
  }

  public void bind(int port) {
    this.port = port;
  }

  /**
   * 设置心跳超时时间，毫秒
   *
   * @param timeout
   */
  public void setHeartbeatTimeout(int timeout) {
    this.timeout = timeout;
  }

  public void start() {
    check();

    boss = new NioEventLoopGroup(2, ThreadUtil.newNamedThreadFactory("netty-boss-", false));
    work = new NioEventLoopGroup(2, ThreadUtil.newNamedThreadFactory("netty-work-", false));
    if (eventListener == null) {
      eventListener = new EventListenerAdapter<>();
    }
    messageHandler = new ServerMessageHandler(messageListener, eventListener);
    ServerBootstrap bootstrap =
        new ServerBootstrap()
            .group(boss, work)
            .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .channel(NioServerSocketChannel.class)
            .childHandler(
                new ChannelInitializer<NioSocketChannel>() {
                  @Override
                  protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline()
                        .addFirst("loggingHandler", new LoggingHandler())
                        .addLast("", new IdleStateHandler(timeout, 0, 0, TimeUnit.MILLISECONDS))
                        .addLast("tcpCodecHandler", tcpCodecHandler)
                        .addLast("messageHandler", messageHandler);
                  }
                });
    bootstrap.bind(port).syncUninterruptibly();
    log.info("绑定端口：" + port + " 成功，可以接收消息了");
  }

  private void check() {
    Objects.requireNonNull(eventListener, "eventListener 不能为 null");
  }

  public void close() {
    NetUtil.close(boss);
    NetUtil.close(work);
  }

  public void writeAndFlush(TcpServerPacketContext<T> ctx, T msg) {
    ctx.getChannel()
        .writeAndFlush(InternalPacket.wrap(msg))
        .addListener(
            f -> {
              if (log.isDebugEnabled()) {
                if (f.isSuccess()) {
                  log.debug("发送： " + msg + " 成功");
                } else {
                  log.error("发送消息：" + msg + " 失败，" + f.cause().getMessage());
                }
              }
            });
  }
}
