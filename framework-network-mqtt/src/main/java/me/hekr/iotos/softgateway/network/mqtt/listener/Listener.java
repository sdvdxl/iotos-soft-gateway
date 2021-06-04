package me.hekr.iotos.softgateway.network.mqtt.listener;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.mqtt.MqttTopicSubscription;
import java.util.List;
import me.hekr.iotos.softgateway.network.mqtt.ConnectionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听器
 *
 * @author iotos
 */
public interface Listener<T> {

  /** 通用log */
  Logger log = LoggerFactory.getLogger(Listener.class);
  /**
   * 建立连接
   *
   * @param context context
   */
  void onConnect(ConnectionContext<T> context);

  /**
   * 断开连接
   *
   * @param context context
   */
  void onDisconnect(ConnectionContext<T> context);

  /**
   * 收到消息
   *
   * @param context context
   * @param topic topic
   * @param qos qos
   * @param payload 消息
   */
  void onMessage(ConnectionContext<T> context, String topic, MqttQoS qos, T payload);

  /**
   * 连接关闭
   *
   * @param context context
   */
  void onClose(ConnectionContext<T> context);

  /**
   * 认证
   *
   * @param context context
   * @return true 认证通过；false 认证失败
   */
  boolean auth(ConnectionContext<T> context);

  /**
   * publish topic acl
   *
   * @param context context
   * @param topic topic
   * @param qoS qoS
   * @return true 允许发布，会进入消息回调；false 不会进入消息回调
   */
  boolean aclPubTopic(ConnectionContext<T> context, String topic, MqttQoS qoS);

  /**
   * 客户端订阅 topic 权限
   *
   * <p>返回的 topic 列表个数要一致，可以根据 clientId 等信息进行判断，赋予不同的 client 不同订阅权限。如果拒绝返回 FAILURE
   *
   * @param context context
   * @param topicSubscriptions 订阅的 topic
   * @return 对应的订阅的 topic 信息
   */
  List<MqttQoS> aclSubTopic(
      ConnectionContext<T> context, List<MqttTopicSubscription> topicSubscriptions);
}
