package me.hekr.iotos.softgateway.network.common.client;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.socket.SocketRuntimeException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.InternalPacket;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;

/** @author iotos */
@Slf4j
public abstract class AbstractClient<T> {
  public final Object LOCK = new Object();
  private final int maxDatagramSize;
  public T result;
  @Setter protected boolean enableNetLog;
  protected EventLoopGroup eventLoop;
  @Setter protected CommonMessageListener<T> messageListener;
  protected ClientMessageHandler<T> clientMessageHandler;
  protected volatile Channel channel;
  /** 命令回复超时时间，毫秒 */
  @Setter protected int timeout = 2000;
  /** 连接超时，毫秒 */
  @Setter protected int connectTimeout = 2000;
  /**
   * 是不是同步；true 同步模式，即发送消息后等待数据返回
   *
   * <p>参考 {@code AbstractClient#setTimeout(int) } 超时时间
   */
  @Setter protected boolean sync;

  @Getter protected int bindPort = 0;
  @Getter protected String host;
  @Getter protected int port;
  protected Class<? extends Channel> channelClass;
  /** 实现类传递 */
  protected PacketCoderHandlerFactory packetCoderHandlerFactory;

  Bootstrap bootstrap;
  /** true 自动重连，仅针对 tcp ，start 方法启动，会阻塞一直到连接成功 */
  @Setter private boolean autoReconnect;

  private EventListener<T> eventListener;

  public AbstractClient(Class<? extends Channel> channelClass) {
    this.channelClass = channelClass;
    this.maxDatagramSize = 2048;
  }

  public AbstractClient(Class<? extends Channel> channelClass, int maxDatagramSize) {
    this.channelClass = channelClass;
    this.maxDatagramSize = maxDatagramSize;
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
    log.info("准备关闭服务 " + this.getClass().getName());
    try {
      preDestroy();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (eventLoop != null) {
      try {
        log.info("关闭 eventLoop");
        eventLoop.shutdownGracefully(0, 10, TimeUnit.SECONDS);
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
      Objects.requireNonNull(messageListener, "async 模式必须设置messageListener");
    }

    if (eventListener == null) {
      eventListener = new EventListenerAdapter<>();
    }

    clientMessageHandler = new ClientMessageHandler<>(this, messageListener, eventListener, sync);

    bootstrap = new Bootstrap();
    eventLoop = new NioEventLoopGroup();

    bootstrap
        .group(eventLoop)
        .channel(channelClass)
        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
        .handler(
            new ChannelInitializer<Channel>() {
              @Override
              protected void initChannel(Channel ch) {
                if (enableNetLog) {
                  ch.pipeline().addLast(new LoggingHandler());
                }

                ch.pipeline()
                    .addLast(packetCoderHandlerFactory.getPacketCoderHandler())
                    .addLast(clientMessageHandler);
              }
            });
    if (DatagramChannel.class.isAssignableFrom(channelClass)) {
      bootstrap.option(
          ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(maxDatagramSize));
      channel = bootstrap.bind(bindPort).syncUninterruptibly().channel();
    } else {
      bootstrap.option(ChannelOption.TCP_NODELAY, true);
      if (autoReconnect) {
        loopConnect();
      } else {
        connect();
      }
    }

    log.info("start 成功");
  }

  /**
   * 发送数据
   *
   * <p>如果设置sync 为 true，会等待返回数据
   *
   * @param t 消息
   *     <p>sync 为 true，等待超时，超时会抛出 {@code TimeoutException}
   * @return sync 为 true 返回 response 数据，否则 null
   */
  @SneakyThrows
  public T send(T t) {
    InternalPacket<T> internalPacket = InternalPacket.wrap(t, new InetSocketAddress(host, port));
    return doSend(internalPacket);
  }

  protected T doSend(InternalPacket<T> internalPacket)
      throws InterruptedException, TimeoutException {
    if (log.isDebugEnabled()) {
      log.debug("发送消息：{}", internalPacket);
    }
    if (!channel.isActive()) {
      throw new SocketRuntimeException("closed");
    }

    if (sync) {
      synchronized (LOCK) {
        result = null;
        // 注意，不能使用  syncUninterruptibly，messageHandler 中也会等待，会造成一个线程的死锁
        channel
            .writeAndFlush(internalPacket)
            .addListener(
                f -> {
                  if (!f.isSuccess()) {
                    log.error(f.cause().getMessage() + "，消息:" + internalPacket.getMessage());
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
        .writeAndFlush(internalPacket)
        .addListener(
            f -> {
              if (!f.isSuccess()) {
                log.error(f.cause().getMessage() + "，消息:" + internalPacket.getMessage());
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

  public void setEventListener(EventListener<T> eventListener) {
    this.eventListener = eventListener;
  }

  /** 直到连接成功 */
  protected void loopConnect() {
    if (eventLoop.isShuttingDown() || eventLoop.isShutdown() || eventLoop.isTerminated()) {
      log.error("服务关闭，不能重连");
      return;
    }
    while (true) {
      try {
        connect();
        return;
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        ThreadUtil.sleep(1000);
      }
    }
  }

  public boolean isConnected() {
    return channel != null && channel.isActive();
  }

  protected synchronized void connect() {
    log.info("尝试连接到 {}:{}", host, port);
    if (isConnected()) {
      log.info("已经连接成功");
      return;
    }
    channel = bootstrap.connect(host, port).syncUninterruptibly().channel();

    log.info("成功连接到 {}:{}", host, port);
  }
}
