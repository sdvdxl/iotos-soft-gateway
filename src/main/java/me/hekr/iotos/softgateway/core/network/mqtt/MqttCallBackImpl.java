package me.hekr.iotos.softgateway.core.network.mqtt;

import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.KlinkDev;
import me.hekr.iotos.softgateway.core.common.klink.processor.KlinkProcessorManager;
import me.hekr.iotos.softgateway.core.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发布消息的回调类
 *
 * <p>必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。 在回调中，将它用来标识已经启动了该回调的哪个实例。 必须在回调类中实现三个方法：
 *
 * <p>public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 *
 * <p>public void connectionLost(Throwable cause)在断开连接时调用。
 *
 * <p>public void deliveryComplete(MqttDeliveryToken token)) 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。 由
 * MqttClient.connect 激活此回调。
 *
 * @author iotos
 */
@Slf4j
@Service
public class MqttCallBackImpl implements MqttCallback {
  @Autowired private List<MqttDisConnectListener> mqttDisConnectListenerList;

  @Autowired private KlinkProcessorManager klinkProcessorManager;
  @Autowired private MqttService mqttService;

  @Override
  @SneakyThrows
  public void connectionLost(Throwable cause) {
    // 连接丢失后，一般在这里面进行重连
    log.warn("软网关已经连接断开,准备开始重连");
    triggerConnectionLost();
    mqttService.init();
  }

  private void triggerConnectionLost() {
    if (mqttDisConnectListenerList != null) {
      for (MqttDisConnectListener listener : mqttDisConnectListenerList) {
        try {
          listener.onDisconnect();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  public void triggerConnectFailed() {
    if (mqttDisConnectListenerList != null) {
      for (MqttDisConnectListener listener : mqttDisConnectListenerList) {
        try {
          listener.onConnectFailed();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    log.info("delivery Complete:" + token.isComplete());
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    log.info("-------------------------------------------------");
    log.info("接收消息主题 : " + topic);
    log.info("接收消息Qos : " + message.getQos());
    log.info("接收消息内容 : " + new String(message.getPayload()));
    log.info("-------------------------------------------------");
    String payload = new String(message.getPayload());

    KlinkDev klinkDev = JsonUtil.fromJson(payload, KlinkDev.class);
    Action action = Action.of(klinkDev.getAction());
    klinkProcessorManager.handle(topic, message, action);
  }
}
