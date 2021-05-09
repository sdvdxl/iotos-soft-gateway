package me.hekr.iotos.softgateway.network.common;

import io.netty.channel.Channel;
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

  @Getter protected final SocketAddress address;
  @Getter protected Channel channel;
  @Setter @Getter protected T message;
  /** 最后发送时间 */
  @Getter @Setter protected LocalDateTime lastSendTime;
  /** 最后接收时间 */
  @Getter @Setter protected LocalDateTime lastReceiveTime;

  protected PacketContext(SocketAddress address, T message) {
    this.address = address;
    this.message = message;
  }

  public static <T> PacketContext<T> wrap(SocketAddress address) {
    return new PacketContext<>(address, null);
  }

  public static <T> PacketContext<T> wrap(SocketAddress address, T message) {
    return new PacketContext<>(address, message);
  }

  public void close() {
    channel.close();
  }
}
