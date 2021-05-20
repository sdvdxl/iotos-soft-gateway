package me.hekr.iotos.softgateway.network.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/** @author iotos */
@Slf4j
public class ConnectionContext<T> {
  MqttEndpoint endpoint;
  @Setter private PacketCoder<T> packetCoder;
  @Setter @Getter private String clientId;
  @Setter @Getter private String username;
  @Setter @Getter private String password;
  @Setter @Getter private InetSocketAddress address;
  @Setter @Getter private LocalDateTime connectTime;
  @Setter @Getter private LocalDateTime authTime;
  @Setter @Getter private boolean auth;

  public void publish(String topic, T payload) {
    publish(topic, MqttQoS.AT_MOST_ONCE, payload);
  }

  public void publish(String topic, MqttQoS qos, T payload) {
    endpoint
        .publish(topic, Buffer.buffer(packetCoder.encode(payload)), qos, false, false)
        .onComplete(
            ar -> {
              if (ar.succeeded() && log.isDebugEnabled()) {
                log.debug("发送消息成功, topic：{}， qos: {} , payload: {}", topic, qos, payload);
              } else {
                log.error(
                    "发送消息失败, topic：{}， qos: {} , payload: {} , error: {}",
                    topic,
                    qos,
                    payload,
                    ar.cause().getMessage());
              }
            });
  }
}
