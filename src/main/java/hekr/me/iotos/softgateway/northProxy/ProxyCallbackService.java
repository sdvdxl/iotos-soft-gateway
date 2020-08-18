package hekr.me.iotos.softgateway.northProxy;

import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.KlinkDev;
import hekr.me.iotos.softgateway.northProxy.processor.ProcessorManager;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
 */
@Slf4j
@Service
public class ProxyCallbackService implements MqttCallback {

  @Autowired private ProcessorManager processorManager;
  @Autowired private ProxyConnectService proxyConnectService;

  @SneakyThrows
  public void connectionLost(Throwable cause) {
    // 连接丢失后，一般在这里面进行重连
    log.warn("软网关已经连接断开,准备开始重连");
    proxyConnectService.connect();
  }

  public void deliveryComplete(IMqttDeliveryToken token) {
    log.info("delivery Complete:" + token.isComplete());
  }

  public void messageArrived(String topic, MqttMessage message) throws Exception {
    log.info("-------------------------------------------------");
    log.info("接收消息主题 : " + topic);
    log.info("接收消息Qos : " + message.getQos());
    log.info("接收消息内容 : " + new String(message.getPayload()));
    log.info("-------------------------------------------------");
    String payload = new String(message.getPayload());

    KlinkDev klinkDev = JsonUtil.fromJson(payload, KlinkDev.class);
    Action action = Action.of(klinkDev.getAction());
    processorManager.handle(topic, message, action);
  }
}
