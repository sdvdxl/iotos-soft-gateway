package me.hekr.iotos.softgateway.northProxy;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.config.ProxyConfig;
import me.hekr.iotos.softgateway.common.constant.Constants;
import me.hekr.iotos.softgateway.utils.JsonUtil;
import me.hekr.iotos.softgateway.utils.ParseUtil;
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
  @Autowired private final ProxyConfig proxyConfig;

  private final MqttClient client;

  private final MqttConnectOptions options;

  @Lazy @Autowired private ProxyCallbackService proxyCallbackService;

  public String getUpTopic() {
    return "up/dev/" + proxyConfig.getDEV_PK() + "/" + proxyConfig.getDEV_ID();
  }

  public String getDownTopic() {
    return "down" + "/dev/" + proxyConfig.getDEV_PK() + "/" + proxyConfig.getDEV_ID();
  }

  @SneakyThrows
  public String getPassword() {
    return ParseUtil.parseByte2HexStr(
        (ParseUtil.HmacSHA1Encrypt(
            proxyConfig.getDEV_PK()
                + proxyConfig.getDEV_ID()
                + proxyConfig.getDEV_SECRET()
                + Constants.RANDOM,
            proxyConfig.getDEV_SECRET())));
  }

  @Autowired
  public ProxyConnectService(ProxyConfig proxyConfig) throws MqttException {
    this.proxyConfig = proxyConfig;
    // MemoryPersistence设置clientid的保存形式，默认为以内存保存
    client =
        new MqttClient(
            proxyConfig.getHOST(),
            "dev:" + proxyConfig.getDEV_PK() + ":" + proxyConfig.getDEV_ID(),
            new MemoryPersistence());
    options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setUserName(Constants.HASH_METHOD + ":" + Constants.RANDOM);
    options.setPassword(getPassword().toCharArray());
    // 设置超时时间
    options.setConnectionTimeout(10);
    // 设置会话心跳时间
    options.setKeepAliveInterval(20);
  }

  // 定义一个主题
  public void connect() {
    log.info("软件网关开始连接");

    try {
      client.setCallback(proxyCallbackService);
      client.connect(options);
      // 订阅
      client.subscribe(getDownTopic(), 0);
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
   * @param message
   * @throws MqttPersistenceException
   * @throws MqttException
   */
  public void publish(Object message) throws MqttPersistenceException, MqttException {
    client.publish(getUpTopic(), new MqttMessage(JsonUtil.toBytes(message)));
    if (log.isInfoEnabled()) {
      log.info("发送消息成功：{}", JsonUtil.toJson(message));
    }
  }

  public boolean isConnected() {
    return client.isConnected();
  }
}
