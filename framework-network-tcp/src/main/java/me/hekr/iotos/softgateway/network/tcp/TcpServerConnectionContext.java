package me.hekr.iotos.softgateway.network.tcp;

import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.common.ConnectionContext;
import me.hekr.iotos.softgateway.network.common.InternalPacket;

/**
 * @author iotos
 */
@Slf4j
public class TcpServerConnectionContext<T> extends ConnectionContext<T> {
  protected final ChannelHandlerContext ctx;
  @Getter @Setter protected CloseReason closeReason;
  @Getter @Setter protected int heartbeatTimeoutCount = 0;
  @Getter @Setter protected LocalDateTime occurTime;

  public void writeAndFlush(T msg) {
    getChannel()
        .writeAndFlush(InternalPacket.wrap(msg))
        .addListener(
            f -> {
              if (log.isDebugEnabled()) {
                if (f.isSuccess()) {
                  log.debug("发送： " + msg + " 成功");
                } else {
                  log.error("发送消息：" + msg + " 失败，" + f.cause().getMessage(), f.cause());
                }
              }
            });
  }

  TcpServerConnectionContext(ChannelHandlerContext ctx, InetSocketAddress address, T message) {
    super(address, message);
    this.ctx = ctx;
    this.channel = ctx.channel();
  }

  public static <T> ConnectionContext<T> wrap(
      ChannelHandlerContext ctx, InetSocketAddress address, T message) {
    return new TcpServerConnectionContext<>(ctx, address, message);
  }

  public static <T> TcpServerConnectionContext<T> wrap(ChannelHandlerContext ctx) {
    return new TcpServerConnectionContext<>(ctx, null, null);
  }

  public static <T> TcpServerConnectionContext<T> wrap(
      ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
    return new TcpServerConnectionContext<>(ctx, remoteAddress, null);
  }

  public void resetHeartbeatTimeoutCount() {
    heartbeatTimeoutCount = 0;
  }

  public void increaseHeartbeatTimeoutCount() {
    heartbeatTimeoutCount++;
  }
}
