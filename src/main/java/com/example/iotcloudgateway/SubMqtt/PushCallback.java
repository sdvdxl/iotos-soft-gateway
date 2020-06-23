package com.example.iotcloudgateway.SubMqtt;

import com.example.iotcloudgateway.dto.TcpPacket;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.TransferPacket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.redisson.api.StreamMessageId;
import org.springframework.beans.factory.annotation.Autowired;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.starter.TioServerBootstrap;
import org.tio.utils.lock.SetWithLock;

import java.util.Map;

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
public class PushCallback implements MqttCallback {

  @Autowired private TioServerBootstrap bootstrap;

  public void connectionLost(Throwable cause) {
    // 连接丢失后，一般在这里面进行重连
    System.out.println("连接断开，可以做重连");
  }

  public void deliveryComplete(IMqttDeliveryToken token) {
    System.out.println("deliveryComplete---------" + token.isComplete());
  }

  public void messageArrived(String topic, MqttMessage message) throws Exception {
    log.info("-------------------------------------------------");
    log.info("接收消息主题 : " + topic);
    log.info("接收消息Qos : " + message.getQos());
    log.info("接收消息内容 : " + new String(message.getPayload()));
    log.info("-------------------------------------------------");
    String payload = new String(message.getPayload());
    TransferPacket packet = JsonUtil.fromJson(payload,TransferPacket.class);
    String userId = packet.getPk() + "@" + packet.getDevId();
    SetWithLock<ChannelContext> byUserid =
            Tio.getByUserid(bootstrap.getServerTioConfig(), userId);

    if (byUserid == null) {
      log.warn(
              "pk:{}, devId:{} context not found, packet: {}",
              packet.getPk(),
              packet.getDevId(),
              payload);
      return;
    }
    TcpPacket resppacket = new TcpPacket();
    resppacket.setBody(Base64.decodeBase64(packet.getPayload()));
    Tio.sendToUser(bootstrap.getServerTioConfig(), userId, resppacket);
  }
}
