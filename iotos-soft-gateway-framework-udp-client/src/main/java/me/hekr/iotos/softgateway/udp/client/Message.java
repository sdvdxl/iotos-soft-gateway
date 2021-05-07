package me.hekr.iotos.softgateway.udp.client;

import java.net.SocketAddress;
import lombok.Getter;

public class Message<T> {
  @Getter T body;
  SocketAddress addr;

  private Message(T o) {
    this.body = o;
  }

  private Message(T body, SocketAddress addr) {
    this.body = body;
    this.addr = addr;
  }

  public static <T> Message<T> wrap(T hello, SocketAddress addr) {
    return new Message<>(hello, addr);
  }

  public static <T> Message<T> wrap(T body) {
    return new Message<>(body);
  }
}
