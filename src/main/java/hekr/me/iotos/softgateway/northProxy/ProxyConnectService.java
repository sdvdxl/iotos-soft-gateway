package hekr.me.iotos.softgateway.northProxy;

import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/** connect方法用来将数据平台软网关连接至IoTOS */
@Slf4j
public class ProxyConnectService {

  public MqttClient client;

  public static String UP_TOPIC = "up/dev/" + ProxyService.DEV_PK + "/" + ProxyService.DEV_ID;

  public static String DOWN_TOPIC = "down" + "/dev/" + ProxyService.DEV_PK + "/" + ProxyService.DEV_ID;
  // 定义MQTT的ID，可以在MQTT服务配置中指定// 网关的clientid
  public static String clientid = "dev:" + ProxyService.DEV_PK + ":" + ProxyService.DEV_ID;

  public ProxyConnectService() throws MqttException {
    // MemoryPersistence设置clientid的保存形式，默认为以内存保存
    client = new MqttClient(ProxyService.HOST, clientid, new MemoryPersistence());
    connect();
  }

  // 定义一个主题
  public void connect() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setCleanSession(true);
    options.setUserName(ProxyService.userName);
    options.setPassword(ProxyService.getPassword().toCharArray());
    // 设置超时时间
    options.setConnectionTimeout(10);
    // 设置会话心跳时间
    options.setKeepAliveInterval(20);
    try {
      client.setCallback(new ProxyCallbackService());
      client.connect(options);
      // 订阅
      client.subscribe(DOWN_TOPIC, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param message
   * @throws MqttPersistenceException
   * @throws MqttException
   */
  public void publish(Object message) throws MqttPersistenceException, MqttException {
    client.publish(UP_TOPIC, new MqttMessage(JsonUtil.toBytes(message)));
    if (log.isInfoEnabled()) {
      log.info("发送消息成功：{}", JsonUtil.toJson(message));
    }
  }

  public boolean isConnected() {
    return client.isConnected();
  }
}
