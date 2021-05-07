package me.hekr.iotos.softgateway.network.common;

import java.net.SocketAddress;
import lombok.Data;

/** @author iotos */
@Data
public class Packet<T> {
  private T message;
  private SocketAddress address;

  private Packet(T o) {
    this.message = o;
  }

  private Packet(T message, SocketAddress addr) {
    this.message = message;
    this.address = addr;
  }

  public static <T> Packet<T> wrap(T hello, SocketAddress addr) {
    return new Packet<>(hello, addr);
  }

  public static <T> Packet<T> wrap(T body) {
    return new Packet<>(body);
  }
}
