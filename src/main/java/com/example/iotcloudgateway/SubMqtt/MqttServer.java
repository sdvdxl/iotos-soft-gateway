package com.example.iotcloudgateway.SubMqtt;

import com.example.iotcloudgateway.SubMqtt.MqttConnect;
import com.example.iotcloudgateway.SubMqtt.SubKLink;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Title:Server 这是发送消息的服务端 配置数据平台相关参数，可执行设备上报数据至平台以及平台下发数据至设备 Description:
 * 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题 此为模拟烟雾传感器连接至智慧消防平台，并且其与IoTOS平台进行交互的代码
 * 添加设备和配置流程详情请见《设备连接软网关数据上报说明书》
 */
@Service
@Slf4j
public class MqttServer {
  /** 以此开始配置设备信息，以及连接参数 */
  // tcp://MQTT安装的服务器地址:MQTT定义的端口号
  // 进入产品中心-产品开发-智慧消防平台软网关，"MQTT接入方式"栏目即可查询
  public static final String HOST = "tcp://106.75.50.110:1883";
  // 软网关的产品pk，进入产品中心-设备管理-智慧消防平台软网关，"产品pk"栏目即可查询
  public static final String DEV_PK = "341c38ea609a4dbbbb8042e0fc436433";
  // 软网关的设备id，进入产品中心-设备管理-智慧消防平台软网关，"产品id"栏目即可查询
  public static final String DEV_ID = "gateway001";
  // 子设备的产品pk，进入产品中心-设备管理-烟雾传感器，"产品pk"栏目即可查询
  public static final String SUBDEV_PK = "c3d0597b499a4e689fb9051b242ed66a";
  // 子设备的设备id，进入产品中心-设备管理-烟雾传感器，"产品id"栏目即可查询
  public static final String SUBDEV_ID = "smokedetector001";
  // 接入至IoTOS的账号密码，生成方式详情请见《设备连接软网关数据上报说明书》
  public static final String userName = "HmacSHA1:init";
  public static final String passWord = "f8219e353ef0f60d3c0a08aee6351890758d1fbc";

  private MqttConnect mqttconnect;

  /**
   * mqtt连接
   *
   * @throws MqttException
   */
  @PostConstruct
  public void mqttconnection() throws MqttException {
    SubKLink klink = new SubKLink();
    mqttconnect = new MqttConnect();
    mqttconnect.message = new MqttMessage();

    mqttconnect.message.setQos(0); // 保证消息能到达一次
    mqttconnect.message.setRetained(false);
    /** 以此开始调用方法，修改kilnk.xxx即可向平台上报不同数据 */
    //
    mqttconnect.message.setPayload(klink.addSub(DEV_PK, DEV_ID, SUBDEV_PK, SUBDEV_ID).getBytes());
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
    log.debug(mqttconnect.message.isRetained() + "------ratained状态");

  }

  @SneakyThrows
  public void addDev(String subDevPk, String subDevId) {
    this.mqttconnect.message.setPayload(SubKLink.addSub(DEV_PK, DEV_ID, subDevPk, subDevId).getBytes());
    this.mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
  @SneakyThrows
  public void devLogin(String subDevPk, String subDevId) {
    this.mqttconnect.message.setPayload(SubKLink.subLogin(subDevPk, subDevId).getBytes());
    this.mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
  @SneakyThrows
  public void devLogout(String subDevPk, String subDevId) {
    this.mqttconnect.message.setPayload(SubKLink.subLogout(subDevPk, subDevId).getBytes());
    this.mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
  @SneakyThrows
  public void devTopo() {
    this.mqttconnect.message.setPayload(SubKLink.subTopo(DEV_PK, DEV_ID).getBytes());
    this.mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
  @SneakyThrows
  public void delDev(String subDevPk, String subDevId) {
    this.mqttconnect.message.setPayload(SubKLink.delSub(DEV_PK, DEV_ID, subDevPk, subDevId).getBytes());
    this.mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
  @SneakyThrows
  public void devSend(String kLink) {
    this.mqttconnect.message.setPayload(kLink.getBytes());
    this.mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
}
