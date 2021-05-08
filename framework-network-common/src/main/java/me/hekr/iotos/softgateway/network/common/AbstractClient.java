package me.hekr.iotos.softgateway.network.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** @author iotos */
@Slf4j
public abstract class AbstractClient<T> {
  public final Object LOCK = new Object();
  public T result;

  protected EventLoopGroup eventLoop;
  @Setter protected MessageListener<T> messageListener;
  protected MessageHandler<T> messageHandler;
  protected Channel channel;
  /** 命令回复超时时间，毫秒 */
  @Setter protected long timeout = 2000;
  /**
   * 是不是同步；true 同步模式，即发送消息后等待数据返回
   *
   * @see #setTimeout(long) 超时时间
   */
  @Setter protected boolean sync;

  @Getter protected int bindPort = 0;
  @Getter protected String host;
  @Getter protected int port;
  protected Class<? extends Channel> channelClass;
  /** 实现类传递 */
  protected ChannelDuplexHandler packetCoderHandler;

  @Setter private LogLevel logLevel = LogLevel.INFO;

  public AbstractClient(Class<? extends Channel> channelClass) {
    this.channelClass = channelClass;
  }

  /**
   * 设置编解码器
   *
   * @param packetCoder 包编解码器
   */
  public abstract void setPacketCoder(PacketCoder<T> packetCoder);

  /** 初始化工作 */
  protected void init() {}

  public void close() {
    try {
      preDestroy();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (eventLoop != null) {
      try {
        eventLoop.shutdownGracefully().syncUninterruptibly();
        log.info(" 成功关闭 client");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /** 准备关闭 */
  protected void preDestroy() {}

  /** 启动 */
  public void start() {
    log.info("init");
    init();
    log.info("init 结束");
    log.info((sync ? "同步" : "异步") + "模式");
    if (sync) {
      if (messageListener != null) {
        log.warn("sync 模式不会调用messageListener");
      }
    } else {
      if (messageListener != null) {
        Objects.requireNonNull(messageListener, "sync 模式必须设置messageListener");
      }
    }

    messageHandler = new MessageHandler<>(this, messageListener, sync);

    Bootstrap bootstrap = new Bootstrap();
    eventLoop = new NioEventLoopGroup();

    bootstrap
        .group(eventLoop)
        .channel(channelClass)
        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .handler(
            new ChannelInitializer<Channel>() {
              @Override
              protected void initChannel(Channel ch) {
                ch.pipeline()
                    .addLast(new LoggingHandler(logLevel))
                    .addLast(packetCoderHandler)
                    .addLast(messageHandler);
              }
            });
    ChannelFuture future;
    if (channelClass == DatagramChannel.class) {
      future = bootstrap.bind(bindPort).awaitUninterruptibly();
    } else {
      bootstrap.option(ChannelOption.TCP_NODELAY, true);
      future = bootstrap.connect(host, port);
    }

    channel = future.syncUninterruptibly().channel();
    log.info("start 成功");
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
        result = null;
        // 注意，不能使用  syncUninterruptibly，messageHandler 中也会等待，会造成一个线程的死锁
        channel
            .writeAndFlush(InternalPacket.wrap(t))
            .addListener(
                f -> {
                  if (!f.isSuccess()) {
                    log.error(f.cause().getMessage() + "，消息:" + t);
                  }
                });

        await(timeout);
        if (result == null) {
          throw new TimeoutException("wait response timeout " + timeout + "ms");
        }

        return result;
      }
    }
    channel
        .writeAndFlush(InternalPacket.wrap(t))
        .addListener(
            f -> {
              if (!f.isSuccess()) {
                log.error(f.cause().getMessage() + "，消息:" + t);
              }
            });
    return null;
  }

  public void signalAll() {
    synchronized (LOCK) {
      LOCK.notifyAll();
    }
  }

  public void await(long timeout) throws InterruptedException {
    synchronized (LOCK) {
      LOCK.wait(timeout);
    }
  }
}
