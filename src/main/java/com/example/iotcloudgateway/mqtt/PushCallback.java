package com.example.iotcloudgateway.mqtt;

import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.tcp.TcpPacket;
import com.example.iotcloudgateway.tcp.TcpServerStarter;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.AddTopoResp;
import iot.cloud.os.core.api.dto.klink.DevLoginResp;
import iot.cloud.os.core.api.dto.klink.GetTopoResp;
import iot.cloud.os.core.api.dto.klink.KlinkResp;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.tio.core.Tio;

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
    TcpPacket resppacket = new TcpPacket();
    KlinkResp klinkResp = JsonUtil.fromJson(payload, KlinkResp.class);
    switch (klinkResp.getAction()) {
      case SubKlinkAction.GET_TOPO_RESP:
        GetTopoResp getTopoResp = JsonUtil.fromJson(payload, GetTopoResp.class);
        getTopoResp
            .getSubs()
            .forEach(
                dev -> {
                  resppacket.setBody(message.getPayload());
                  Tio.sendToUser(
                      TcpServerStarter.tioServer.getServerTioConfig(),
                      dev.getPk() + "@" + dev.getDevId(),
                      resppacket);
                });
        return;
      case SubKlinkAction.DEV_LOGIN_RESP:
        DevLoginResp devLoginResp = JsonUtil.fromJson(payload, DevLoginResp.class);
        resppacket.setBody(message.getPayload());
        Tio.sendToUser(
            TcpServerStarter.tioServer.getServerTioConfig(),
            devLoginResp.getPk() + "@" + devLoginResp.getDevId(),
            resppacket);
        return;
      case SubKlinkAction.ADD_TOPO_RESP:
        AddTopoResp addTopoResp = JsonUtil.fromJson(payload, AddTopoResp.class);
        resppacket.setBody(message.getPayload());
        Tio.sendToUser(
            TcpServerStarter.tioServer.getServerTioConfig(),
            addTopoResp.getSub().getPk() + "@" + addTopoResp.getSub().getDevId(),
            resppacket);
        return;
      default:
    }
  }
}
