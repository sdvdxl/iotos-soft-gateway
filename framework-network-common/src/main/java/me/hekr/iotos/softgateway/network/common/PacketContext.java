package me.hekr.iotos.softgateway.network.common;

import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/** @author iotos */
@Slf4j
@ToString
public class PacketContext<T> {

  final ChannelHandlerContext ctx;
  @Getter private final SocketAddress address;
  @Setter @Getter private T message;
  /** 最后发送时间 */
  @Getter @Setter private LocalDateTime lastSendTime;
  /** 最后接收时间 */
  @Getter @Setter private LocalDateTime lastReceiveTime;

  @Getter @Setter private CloseReason closeReason;
  @Getter @Setter private int heartbeatTimeoutCount = 0;
  @Getter @Setter private LocalDateTime occurTime;

  PacketContext(ChannelHandlerContext ctx, SocketAddress address, T message) {
    this.ctx = ctx;
    this.address = address;
    this.message = message;
  }

  public static <T> PacketContext<T> wrap(
      ChannelHandlerContext ctx, SocketAddress address, T message) {
    return new PacketContext<>(ctx, address, message);
  }

  public static <T> PacketContext<T> wrap(ChannelHandlerContext ctx) {
    return new PacketContext<>(ctx, null, null);
  }

  public static <T> PacketContext<T> wrap(ChannelHandlerContext ctx, SocketAddress remoteAddress) {
    return new PacketContext<>(ctx, remoteAddress, null);
  }

  public void resetHeartbeatTimeoutCount() {
    heartbeatTimeoutCount = 0;
  }

  public void increaseHeartbeatTimeoutCount() {
    heartbeatTimeoutCount++;
  }

  public void writeAndFlush(T msg) {
    ctx.writeAndFlush(InternalPacket.wrap(msg))
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

  public void close(CloseReason closeReason) {
    this.closeReason = closeReason;
    ctx.close();
  }

  /** server 端主动关闭 */
  public void close() {
    this.closeReason = CloseReason.SERVER_CLOSED;
    ctx.close();
  }
}
