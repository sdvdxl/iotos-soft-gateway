package me.hekr.iotos.softgateway.network.mqtt;

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.mqtt.MqttAuth;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttTopicSubscription;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.common.coder.PacketCoder;
import me.hekr.iotos.softgateway.network.mqtt.listener.Listener;

/** @author iotos */
@Slf4j
public class MqttServer<T> {
  private final int poolSize = Runtime.getRuntime().availableProcessors() * 4;
  @Setter PacketCoder<T> packetCoder;
  private Vertx vertx;
  private io.vertx.mqtt.MqttServer server;
  @Setter private int port;
  @Setter private Listener<T> listener;

  public MqttServer(int port) {
    this.port = port;
  }

  public MqttServer() {
    this(1883);
  }

  public void start() {
    Objects.requireNonNull(packetCoder, "packetCoder 必填");
    Objects.requireNonNull(listener, "listener 必填");
    VertxOptions options = new VertxOptions();
    options.setEventLoopPoolSize(poolSize);
    options.setWorkerPoolSize(poolSize);
    vertx = Vertx.vertx(options);
    server = io.vertx.mqtt.MqttServer.create(vertx);
    server.exceptionHandler(t -> log.error(t.getMessage(), t));
    server.endpointHandler(
        endpoint -> {
          ConnectionContext<T> context = MqttConnections.add(endpoint);
          context.setPacketCoder(packetCoder);
          endpoint.autoKeepAlive(true);
          endpoint.publishAutoAck(true);
          handleClose(endpoint);
          handlePublishMessage(endpoint);
          handleAuth(endpoint);
          handleException(endpoint);
          handleSubscribe(endpoint);
        });
    server
        .listen(1883, "localhost")
        .onComplete(
            ar -> {
              if (ar.succeeded()) {
                log.info("mqtt server 启动成功，绑定端口：{}", server.actualPort());
              } else {
                log.error("mqtt server 启动失败，" + ar.cause().getMessage(), ar.cause());
              }
            });
  }

  /**
   * 订阅 topic 认证
   *
   * @param endpoint 连接信息
   */
  private void handleSubscribe(MqttEndpoint endpoint) {
    endpoint.subscribeHandler(
        m -> {
          ConnectionContext<T> context = MqttConnections.get(endpoint);
          List<MqttQoS> qosList = listener.aclSubTopic(context, m.topicSubscriptions());
          if (log.isDebugEnabled()) {
            for (int i = 0; i < m.topicSubscriptions().size(); i++) {
              MqttTopicSubscription topic = m.topicSubscriptions().get(i);
              MqttQoS qos = qosList.get(i);
              log.debug(
                  (qos == MqttQoS.FAILURE ? "不允许 " : "允许 ")
                      + context.getClientId()
                      + " 订阅 "
                      + topic.topicName()
                      + " qos:"
                      + topic.qualityOfService()
                      + " 协商 qos:"
                      + qos);
            }
          }

          endpoint.subscribeAcknowledge(m.messageId(), qosList);
        });
  }

  private void handleException(MqttEndpoint endpoint) {
    endpoint.exceptionHandler(t -> log.error(t.getMessage(), t));
  }

  /**
   * 处理关闭连接
   *
   * @param endpoint 连接信息
   */
  private void handleClose(MqttEndpoint endpoint) {
    endpoint.closeHandler(v -> listener.onClose(MqttConnections.remove(endpoint)));
  }

  /**
   * 处理客户端发布的消息
   *
   * @param endpoint 连接信息
   */
  @SuppressWarnings("unchecked")
  private void handlePublishMessage(MqttEndpoint endpoint) {
    endpoint.publishHandler(
        m -> {
          ConnectionContext<T> context = MqttConnections.get(endpoint);
          boolean pass = listener.aclPubTopic(context, m.topicName(), m.qosLevel());
          if (!pass) {
            if (log.isDebugEnabled()) {
              log.debug(
                  "publish topic 拒绝，丢弃消息, clientId: {}, username: {}, topic: {}, qos:{}",
                  context.getClientId(),
                  context.getUsername(),
                  m.topicName(),
                  m.qosLevel());
              return;
            }
          }
          listener.onMessage(
              context,
              m.topicName(),
              m.qosLevel(),
              (T) packetCoder.decode(m.payload().getBytes()).getResult());
        });
  }

  /**
   * 处理鉴权
   *
   * @param endpoint 连接信息
   */
  private void handleAuth(MqttEndpoint endpoint) {
    MqttAuth auth = endpoint.auth();
    String username = auth == null ? null : auth.getUsername();
    String password = auth == null ? null : auth.getPassword();
    ConnectionContext<T> context = MqttConnections.get(endpoint);
    context.setUsername(username);
    context.setPassword(password);
    boolean authResult = listener.auth(context);
    context.setAuth(authResult);
    context.setAuthTime(LocalDateTime.now());
    if (authResult) {
      endpoint.accept(false);
    } else {
      endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD);
    }
  }

  public void close() {
    if (server != null) {
      server.close();
    }

    if (vertx != null) {
      vertx.close();
    }
  }
}
