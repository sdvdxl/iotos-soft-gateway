package me.hekr.iotos.softgateway.network.common;

import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/** @author iotos */
@Slf4j
@ToString
public class ConnectionContext<T> {

  @Getter protected final InetSocketAddress address;
  @Getter protected Channel channel;
  @Setter @Getter protected T message;
  /** 最后发送时间 */
  @Getter @Setter protected LocalDateTime lastSendTime;
  /** 最后接收时间 */
  @Getter @Setter protected LocalDateTime lastReceiveTime;

  protected ConnectionContext(InetSocketAddress address, T message) {
    this.address = address;
    this.message = message;
  }

  public static <T> ConnectionContext<T> wrap(InetSocketAddress address) {
    return new ConnectionContext<>(address, null);
  }

  public static <T> ConnectionContext<T> wrap(InetSocketAddress address, T message) {
    return new ConnectionContext<>(address, message);
  }

  public void close() {
    channel.close();
  }
}
