package me.hekr.iotos.softgateway.sample;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.mqtt.MqttTopicSubscription;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.network.mqtt.ConnectionContext;
import me.hekr.iotos.softgateway.network.mqtt.MqttServer;
import me.hekr.iotos.softgateway.network.mqtt.PacketCoder;
import me.hekr.iotos.softgateway.network.mqtt.listener.AcceptAllConnectionListenerAdapter;

/** @author iotos */
@Slf4j
public class MqttServerSample {
  public static void main(String[] args) {
    try {
      MqttServer<String> mqttServer = new MqttServer<>();
      mqttServer.setPacketCoder(PacketCoder.STRING_CODER);
      mqttServer.setListener(
          new AcceptAllConnectionListenerAdapter<String>() {
            @Override
            public boolean aclPubTopic(
                ConnectionContext<String> context, String topic, MqttQoS qoS) {
              // 自定义 publish acl
              // 如果是 admin/# 只允许 username 是 admin 的发布，否则不允许
              if (topic.startsWith("admin")) {
                return "admin".equals(context.getUsername());
              }

              return true;
            }

            @Override
            public List<MqttQoS> aclSubTopic(
                ConnectionContext<String> context, List<MqttTopicSubscription> topicSubscriptions) {
              // 自定义 publish acl
              // 如果是 admin/# 只允许 username 是 admin 的发布，否则不允许
              return topicSubscriptions.stream()
                  .map(
                      t -> {
                        if (t.topicName().startsWith("admin")) {
                          boolean allow = "admin".equals(context.getUsername());
                          return allow ? t.qualityOfService() : MqttQoS.FAILURE;
                        }

                        return t.qualityOfService();
                      })
                  .collect(Collectors.toList());
            }
          });
      mqttServer.start();
      new CountDownLatch(1).await();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
