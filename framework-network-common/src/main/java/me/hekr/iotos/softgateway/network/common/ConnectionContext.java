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
 * <p>ConnectionContext class.</p>
 *
 * @author iotos
 * @version $Id: $Id
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

  /**
   * <p>Constructor for ConnectionContext.</p>
   *
   * @param address a {@link java.net.InetSocketAddress} object.
   * @param message a T object.
   */
  protected ConnectionContext(InetSocketAddress address, T message) {
    this.address = address;
    this.message = message;
    this.attr = new ConcurrentHashMap<>();
  }

  /**
   * <p>put.</p>
   *
   * @param key a {@link java.lang.Object} object.
   * @param value a {@link java.lang.Object} object.
   */
  public void put(Object key, Object value) {
    channel.attr(AttributeKey.newInstance(String.valueOf(key))).set(value);
  }

  /**
   * <p>get.</p>
   *
   * @param key a {@link java.lang.Object} object.
   * @return a {@link java.lang.Object} object.
   */
  public Object get(Object key) {
    return channel.attr(AttributeKey.newInstance(String.valueOf(key))).get();
  }

  /**
   * <p>wrap.</p>
   *
   * @param address a {@link java.net.InetSocketAddress} object.
   * @param <T> a T object.
   * @return a {@link me.hekr.iotos.softgateway.network.common.ConnectionContext} object.
   */
  public static <T> ConnectionContext<T> wrap(InetSocketAddress address) {
    return new ConnectionContext<>(address, null);
  }

  /**
   * <p>wrap.</p>
   *
   * @param address a {@link java.net.InetSocketAddress} object.
   * @param message a T object.
   * @param <T> a T object.
   * @return a {@link me.hekr.iotos.softgateway.network.common.ConnectionContext} object.
   */
  public static <T> ConnectionContext<T> wrap(InetSocketAddress address, T message) {
    return new ConnectionContext<>(address, message);
  }

  /**
   * <p>close.</p>
   */
  public void close() {
    channel.close();
  }

  /**
   * <p>Setter for the field <code>connectTime</code>.</p>
   */
  public void setConnectTime() {
    this.connectTime = LocalDateTime.now();
  }
}
