package me.hekr.iotos.softgateway.network.common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author iotos
 */
@Slf4j
@ToString
public class ConnectionContext<T> {

  @Getter protected final InetSocketAddress address;
  private Map<Object, Object> attr;
  @Getter protected Channel channel;
  @Setter @Getter protected T message;
  /** 最后发送时间 */
  @Getter @Setter protected LocalDateTime lastSendTime;
  /** 最后接收时间 */
  @Getter @Setter protected LocalDateTime lastReceiveTime;
  /** 链接时间 */
  protected LocalDateTime connectTime;

  protected ConnectionContext(InetSocketAddress address, T message) {
    this.address = address;
    this.message = message;
    this.attr = new ConcurrentHashMap<>();
  }

  public void put(Object key, Object value) {
    channel.attr(AttributeKey.newInstance(String.valueOf(key))).set(value);
  }

  public Object get(Object key) {
    return channel.attr(AttributeKey.newInstance(String.valueOf(key))).get();
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

  public void setConnectTime() {
    this.connectTime = LocalDateTime.now();
  }
}
