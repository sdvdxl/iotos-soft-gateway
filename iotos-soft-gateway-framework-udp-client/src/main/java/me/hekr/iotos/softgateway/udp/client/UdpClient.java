package me.hekr.iotos.softgateway.udp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * UDP client
 *
 * <p>支持异步和同步模式
 *
 * <p>同步模式不需要设置 UdpMessageListener；要求一问一答模式，即发送请求，回复请求，可以配合超时时间，默认2s
 *
 * <p>异步模式需要设置 UdpMessageListener，不严格要求是一问一答模式，有消息收到就会进入回调函数
 *
 * @author iotos
 */
@Slf4j
public class UdpClient<T> {
  final Object LOCK = new Object();
  T result;
  private EventLoopGroup eventLoop;
  /** 命令回复超时时间，毫秒 */
  @Setter private long timeout = 2000;
  /**
   * 是不是同步；true 同步模式，即发送消息后等待数据返回
   *
   * @see #setTimeout(long) 超时时间
   */
  @Setter private boolean sync;

  @Setter private String host;
  @Setter private int port;
  @Setter private LogLevel logLevel = LogLevel.INFO;
  private DatagramChannel channel;
  @Setter private UdpCoder<T> udCoder;
  private Codec<T> codec;
  @Setter private UdpMessageListener<T> messageListener;
  private UdpMessageHandler<T> udpMessageHandler;
  @Setter private int bindPort = 0;

  public void init() {
    if (sync) {
      log.warn("sync 模式不会调用messageListener");
    } else {
      if (messageListener != null) {
        Objects.requireNonNull(messageListener, "sync 模式必须设置messageListener");
      }
    }

    codec = new Codec<>(udCoder);
    codec.host = this.host;
    codec.port = this.port;

    udpMessageHandler = new UdpMessageHandler<>(this, messageListener, sync);

    start();
  }

  public void start() {
    Bootstrap bootstrap = new Bootstrap();
    eventLoop = new NioEventLoopGroup(4);
    ChannelFuture future =
        bootstrap
            .group(eventLoop)
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .handler(
                new ChannelInitializer<NioDatagramChannel>() {
                  @Override
                  protected void initChannel(NioDatagramChannel ch) throws Exception {
                    ch.pipeline()
                        .addLast(new LoggingHandler(logLevel))
                        .addLast(codec)
                        .addLast(udpMessageHandler);
                  }
                })
            .bind(bindPort)
            .awaitUninterruptibly();
    channel = (DatagramChannel) future.awaitUninterruptibly().channel();
  }

  public void close() {
    if (eventLoop != null) {
      eventLoop.shutdownGracefully();
    }
    log.info(" 成功关闭 client");
  }

  /**
   * 发送数据
   *
   * <p>如果设置sync 为 true，会等待返回数据
   *
   * @throws TimeoutException sync 为 true，等待超时
   * @return sync 为 true 返回 response 数据，否则 null
   */
  @SneakyThrows
  public T send(T t) {
    if (sync) {
      synchronized (LOCK) {
        channel.writeAndFlush(Message.wrap(t)).syncUninterruptibly();

        LOCK.wait(timeout);
        if (result == null) {
          throw new TimeoutException("wait response timeout " + timeout + "ms");
        }

        return result;
      }
    }
    channel.writeAndFlush(Message.wrap(t)).syncUninterruptibly();
    return null;
  }
}
