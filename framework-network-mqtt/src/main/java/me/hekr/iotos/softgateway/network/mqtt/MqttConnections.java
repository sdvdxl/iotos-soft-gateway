package me.hekr.iotos.softgateway.network.mqtt;

import io.vertx.core.net.impl.SocketAddressImpl;
import io.vertx.mqtt.MqttEndpoint;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MqttConnections {
  private static final ConcurrentMap<MqttEndpoint, ConnectionContext<?>> CONTEXTS =
      new ConcurrentHashMap<>();

  public static <T> ConnectionContext<T> add(MqttEndpoint endpoint) {
    ConnectionContext<T> context = new ConnectionContext<>();
    context.setClientId(endpoint.clientIdentifier());
    context.endpoint = endpoint;
    SocketAddressImpl socketAddress = (SocketAddressImpl) endpoint.remoteAddress();
    context.setAddress(new InetSocketAddress(socketAddress.host(), socketAddress.port()));
    context.setConnectTime(LocalDateTime.now());
    CONTEXTS.put(endpoint, context);
    return context;
  }

  public static <T> ConnectionContext<T> remove(MqttEndpoint endpoint) {
    return (ConnectionContext<T>) CONTEXTS.remove(endpoint);
  }

  public static <T> ConnectionContext<T> get(MqttEndpoint endpoint) {
    return (ConnectionContext<T>) CONTEXTS.get(endpoint);
  }
}
