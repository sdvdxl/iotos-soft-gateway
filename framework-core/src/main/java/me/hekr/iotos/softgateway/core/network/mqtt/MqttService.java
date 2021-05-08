package me.hekr.iotos.softgateway.core.network.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.config.MqttConfig;
import me.hekr.iotos.softgateway.core.network.mqtt.listener.MqttConnectedListener;
import me.hekr.iotos.softgateway.core.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * connect方法用来将数据平台软网关连接至IoTOS
 *
 * @author iotos
 */
@Slf4j
@Component
public class MqttService {
  private static final int MAX_RETRY_COUNT = 3;
  private final IotOsConfig iotOsConfig;
  private final BlockingQueue<Object> queue = new ArrayBlockingQueue<>(1000);
  private final ExecutorService publishExecutor =
      Executors.newSingleThreadExecutor(ThreadUtil.newNamedThreadFactory("publishExecutor", false));
  private final AtomicInteger connectConut = new AtomicInteger();
  @Autowired private List<MqttConnectedListener> mqttConnectedListeners;
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
    log.info("软件网关开始连接");

    try {
      client.setCallback(mqttCallBackImpl);
      client.connect(options);
      log.info("软件网关开始连接连接成功！");
      connectConut.incrementAndGet();

      triggerConnectedListeners();
      // 订阅
      client.subscribe(iotOsConfig.getGatewayConfig().getDownTopic(), 0);
    } catch (Exception e) {
      log.error("软件网关连接失败！" + e.getMessage(), e);
      mqttCallBackImpl.triggerConnectFailed();
    }
  }

  private void triggerConnectedListeners() {
    boolean firstConnected = connectConut.get() == 1;
    if (mqttConnectedListeners != null) {
      for (MqttConnectedListener listener : mqttConnectedListeners) {
        if (firstConnected) {
          try {
            listener.onConnected();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        } else {
          try {
            listener.onReConnected();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
      }
    }
  }

  private void closeMqttClient() {
    if (client != null) {
      try {
        client.disconnect();
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
      if (!isDisconnected()) {
        return;
      }

      try {
        TimeUnit.SECONDS.sleep(iotOsConfig.getMqttConfig().getConnectTimeout() + 3);
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
      Object msg = null;
      try {
        msg = queue.take();
      } catch (InterruptedException ignored) {
      }
      trySend(msg);
    }
  }

  private void trySend(Object msg) {
    // 报错就重试
    for (int i = 0; i < MAX_RETRY_COUNT; i++) {
      try {
        doPublish(msg);
        break;
      } catch (MqttException e) {
        log.error("mqtt发布报错：" + e.getMessage() + ",第 " + (i + 1) + "次重试", e);
        ThreadUtil.sleep(1000);
      }
    }
  }

  private void doPublish(Object message) throws MqttException {
    if (log.isDebugEnabled()) {
      log.debug("发送消息：{}", JsonUtil.toJson(message));
    }

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