package me.hekr.iotos.softgateway.network.tcp;

import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.CloseReason;
import me.hekr.iotos.softgateway.network.common.PacketContext;

/** @author iotos */
@Slf4j
public class TcpServerPacketContext<T> extends PacketContext<T> {
  protected final ChannelHandlerContext ctx;
  @Getter @Setter protected CloseReason closeReason;
  @Getter @Setter protected int heartbeatTimeoutCount = 0;
  @Getter @Setter protected LocalDateTime occurTime;

  TcpServerPacketContext(ChannelHandlerContext ctx, SocketAddress address, T message) {
    super(address, message);
    this.ctx = ctx;
    this.channel = ctx.channel();
  }

  public static <T> PacketContext<T> wrap(
      ChannelHandlerContext ctx, SocketAddress address, T message) {
    return new TcpServerPacketContext<>(ctx, address, message);
  }

  public static <T> TcpServerPacketContext<T> wrap(ChannelHandlerContext ctx) {
    return new TcpServerPacketContext<>(ctx, null, null);
  }

  public static <T> TcpServerPacketContext<T> wrap(
      ChannelHandlerContext ctx, SocketAddress remoteAddress) {
    return new TcpServerPacketContext<>(ctx, remoteAddress, null);
  }

  public void resetHeartbeatTimeoutCount() {
    heartbeatTimeoutCount = 0;
  }

  public void increaseHeartbeatTimeoutCount() {
    heartbeatTimeoutCount++;
  }
}
