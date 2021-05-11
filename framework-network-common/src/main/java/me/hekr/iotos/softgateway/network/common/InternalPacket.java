package me.hekr.iotos.softgateway.network.common;

import java.net.InetSocketAddress;
import lombok.Data;

/** @author iotos */
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

  public static <T> InternalPacket<T> wrap(T msg, InetSocketAddress addr) {
    return new InternalPacket<>(msg, addr);
  }

  public static <T> InternalPacket<T> wrap(T body) {
    return new InternalPacket<>(body);
  }
}
