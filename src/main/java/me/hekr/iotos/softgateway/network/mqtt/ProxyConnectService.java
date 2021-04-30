package me.hekr.iotos.softgateway.network.mqtt;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.config.IotOsConfig;
import me.hekr.iotos.softgateway.common.config.MqttConfig;
import me.hekr.iotos.softgateway.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** connect方法用来将数据平台软网关连接至IoTOS */
@Slf4j
@Component
public class ProxyConnectService {
  @Autowired private final IotOsConfig iotOsConfig;

  private final MqttClient client;

  private final MqttConnectOptions options;

  @Lazy @Autowired private ProxyCallbackService proxyCallbackService;

  @Autowired
  public ProxyConnectService(IotOsConfig iotOsConfig) throws MqttException {
    this.iotOsConfig = iotOsConfig;
    MqttConfig mqtt = iotOsConfig.getMqttConfig();
    // MemoryPersistence设置clientid的保存形式，默认为以内存保存
    client = new MqttClient(mqtt.getEndpoint(), mqtt.getClientId(), new MemoryPersistence());
    options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setUserName(mqtt.getUsername());
    options.setPassword(mqtt.getPassword());
    // 设置超时时间
    options.setConnectionTimeout(mqtt.getConnectTimeout());
    // 设置会话心跳时间
    options.setKeepAliveInterval(mqtt.getKeepAliveTime());
  }

  // 定义一个主题
  public void connect() {
    log.info("软件网关开始连接");

    try {
      client.setCallback(proxyCallbackService);
      client.connect(options);
      // 订阅
      client.subscribe(iotOsConfig.getGatewayConfig().getDownTopic(), 0);
    } catch (Exception e) {
      log.warn("软件网关连接失败", e);
    }
  }

  @PostConstruct
  public void init() throws MqttException {
    while (!isConnected()) {
      log.info("正在链接...");
      connect();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * @param message 消息，发送的时候会被 toJson
   * @throws MqttPersistenceException
   * @throws MqttException
   */
  public void publish(Object message) throws MqttPersistenceException, MqttException {
    client.publish(
        iotOsConfig.getGatewayConfig().getUpTopic(), new MqttMessage(JsonUtil.toBytes(message)));
    if (log.isInfoEnabled()) {
      log.info("发送消息成功：{}", JsonUtil.toJson(message));
    }
  }

  public boolean isConnected() {
    return client.isConnected();
  }
}
