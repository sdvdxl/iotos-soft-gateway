package me.hekr.iotos.softgateway.network.common;

import java.net.InetSocketAddress;
import lombok.Data;

/**
 * <p>InternalPacket class.</p>
 *
 * @version $Id: $Id
 */
@Data
public class InternalPacket<T> {
  private T message;
  private InetSocketAddress address;

  private InternalPacket(T o) {
    this.message = o;
  }

  private InternalPacket(T message, InetSocketAddress addr) {
    this.message = message;
    this.address = addr;
  }

  /**
   * <p>wrap.</p>
   *
   * @param msg a T object.
   * @param addr a {@link java.net.InetSocketAddress} object.
   * @param <T> a T object.
   * @return a {@link me.hekr.iotos.softgateway.network.common.InternalPacket} object.
   */
  public static <T> InternalPacket<T> wrap(T msg, InetSocketAddress addr) {
    return new InternalPacket<>(msg, addr);
  }

  /**
   * <p>wrap.</p>
   *
   * @param body a T object.
   * @param <T> a T object.
   * @return a {@link me.hekr.iotos.softgateway.network.common.InternalPacket} object.
   */
  public static <T> InternalPacket<T> wrap(T body) {
    return new InternalPacket<>(body);
  }
}
