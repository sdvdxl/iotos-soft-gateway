package me.hekr.iotos.softgateway.network.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.config.IotOsConfig;
import me.hekr.iotos.softgateway.common.config.MqttConfig;
import me.hekr.iotos.softgateway.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** connect方法用来将数据平台软网关连接至IoTOS */
@Slf4j
@Component
public class MqttService {
  private final IotOsConfig iotOsConfig;
  private final BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1000);
  private final ExecutorService publishExecutor =
      Executors.newSingleThreadExecutor(ThreadUtil.newNamedThreadFactory("publishExecutor", false));
  private MqttClient client;
  private MqttConnectOptions options;
  @Lazy @Autowired private MqttCallBackImpl mqttCallBackImpl;

  @Autowired
  public MqttService(IotOsConfig iotOsConfig) throws MqttException {
    this.iotOsConfig = iotOsConfig;
    initConfig(iotOsConfig);
    publishExecutor.execute(this::startPublishTask);
  }

  private void initConfig(IotOsConfig iotOsConfig) throws MqttException {
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

  public void connect() {
    closeMqttClient();
    log.info("软件网关开始连接");

    try {
      client.setCallback(mqttCallBackImpl);
      client.connect(options);
      log.info("软件网关开始连接连接成功！");
      // 订阅
      client.subscribe(iotOsConfig.getGatewayConfig().getDownTopic(), 0);
    } catch (Exception e) {
      log.warn("软件网关连接失败！", e);
    }
  }

  private void closeMqttClient() {
    if (client != null) {
      try {
        client.close(true);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  public void init() {
    while (isDisconnected()) {
      log.info("正在链接...");
      connect();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  /** @param message 消息，发送的时候会被 toJson */
  @SneakyThrows
  public void publish(Object message) {
    queue.add(message);
  }

  public boolean isDisconnected() {
    return !client.isConnected();
  }

  private void startPublishTask() {
    while (!Thread.currentThread().isInterrupted()) {
      if (isDisconnected()) {
        try {
          TimeUnit.SECONDS.sleep(1);
          continue;
        } catch (InterruptedException ignored) {
        }
      }
      try {
        doPublish(queue.take());
      } catch (MqttException e) {
        log.error("publish error, " + e.getMessage(), e);
      } catch (InterruptedException ignored) {
      }
    }
  }

  private void doPublish(Object message) throws MqttException {
    client.publish(
        iotOsConfig.getGatewayConfig().getUpTopic(), new MqttMessage(JsonUtil.toBytes(message)));
    if (log.isInfoEnabled()) {
      log.info("发送消息成功：{}", JsonUtil.toJson(message));
    }
  }

  public void close() {
    closeMqttClient();
    try {
      publishExecutor.shutdown();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
