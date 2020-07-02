package com.example.iotcloudgateway.mqtt;

import com.example.iotcloudgateway.constant.Constants;
import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.enums.Action;
import com.example.iotcloudgateway.klink.AddTopo;
import com.example.iotcloudgateway.klink.DelTopo;
import com.example.iotcloudgateway.klink.DevLogin;
import com.example.iotcloudgateway.klink.DevLogout;
import com.example.iotcloudgateway.klink.GetTopo;
import com.example.iotcloudgateway.klink.KlinkDev;
import com.example.iotcloudgateway.klink.Register;
import com.example.iotcloudgateway.klink.TopoSub;
import com.example.iotcloudgateway.utils.JsonUtil;
import com.example.iotcloudgateway.utils.ParseUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Title:Server 这是发送消息的服务端 配置数据平台相关参数，可执行设备上报数据至平台以及平台下发数据至设备 Description:
 * 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题 此为模拟烟雾传感器连接至智慧消防平台，并且其与IoTOS平台进行交互的代码
 * 添加设备和配置流程详情请见《设备连接软网关数据上报说明书》
 */
@Slf4j
public class MqttServer {
  /** 以此开始配置设备信息，以及连接参数 */
  // tcp://MQTT安装的服务器地址:MQTT定义的端口号
  // 进入产品中心-产品开发-智慧消防平台软网关，"MQTT接入方式"栏目即可查询
  public static final String HOST = "tcp://106.75.50.110:1883";
  // 软网关的产品pk，进入产品中心-设备管理-智慧消防平台软网关，"产品pk"栏目即可查询
  public static final String DEV_PK = "fc5dbdd26fee4688a6ab35b63a294cc1";
  // 软网关的设备id，进入产品中心-设备管理-智慧消防平台软网关，"产品id"栏目即可查询
  public static final String DEV_ID = "gatewaydemo";
  // 接入至IoTOS的账号密码，生成方式详情请见《设备连接软网关数据上报说明书》
  public static final String userName = "HmacSHA1:init";
  public static final String passWord = "18c78b6dfa4fde73c1a03c888842bf4b5bbefbea";

  // 子设备的产品pk，进入产品中心-设备管理-烟雾传感器，"产品pk"栏目即可查询
  public static final String SUBDEV_PK = "c3d0597b499a4e689fb9051b242ed66a";
  // 子设备的设备id，进入产品中心-设备管理-烟雾传感器，"产品id"栏目即可查询
  public static final String SUBDEV_ID = "smokedetector001";

  private static MqttConnect mqttconnect;

  /**
   * mqtt连接
   *
   * @throws MqttException
   */
  public static void mqttconnection() throws MqttException {
    mqttconnect = new MqttConnect();
    mqttconnect.message = new MqttMessage();

    mqttconnect.message.setQos(0); // 保证消息能到达一次
    mqttconnect.message.setRetained(true);
    log.debug(mqttconnect.message.isRetained() + "------ratained状态");
  }

  public static void sendKlink(KlinkDev klinkDev) {
    switch (Action.of(klinkDev.getAction())) {
      case REGISTER:
        register(
            klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getProductSecret(), klinkDev.getName());
        break;
      case ADD_TOPO:
        addDev(klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getDevSecret());
        break;
      case DEV_LOGIN:
        addDev(klinkDev.getPk(), klinkDev.getDevId(), klinkDev.getDevSecret());
        devLogin(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case DEV_LOGOUT:
        devLogout(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case GET_TOPO:
        getTopo();
        break;
      case DEL_TOPO:
        delDev(klinkDev.getPk(), klinkDev.getDevId());
        break;
      case DEV_SEND:
        devSend(JsonUtil.toJson(klinkDev));
        break;
      case HEARTBEAT:
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + klinkDev.getAction());
    }
  }

  @SneakyThrows
  public void publish(byte[] message) {
    mqttconnect.message.setPayload(message);
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 动态注册设备 */
  @SneakyThrows
  private static void register(
      String subDevPk, String subDevId, String productSecret, String devName) {
    Register register = new Register();
    register.setDevId(subDevId);
    register.setPk(subDevPk);
    register.setName(devName);
    register.setRandom(Constants.RANDOM);
    register.setHashMethod(Constants.HASH_METHOD);
    register.setSign(
        ParseUtil.parseByte2HexStr(
            ParseUtil.HmacSHA1Encrypt(subDevPk + productSecret + Constants.RANDOM, productSecret)));
    mqttconnect.message.setPayload(JsonUtil.toBytes(register));
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 设备拓扑 */
  @SneakyThrows
  public static void addDev(String subDevPk, String subDevId, String subDevSecret) {
    AddTopo addTopo = new AddTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(subDevPk);
    topoSub.setDevId(subDevId);
    topoSub.setHashMethod(Constants.HASH_METHOD);
    topoSub.setRandom(Constants.RANDOM);
    topoSub.setSign(
        ParseUtil.parseByte2HexStr(
            ParseUtil.HmacSHA1Encrypt(
                subDevPk + subDevId + subDevSecret + Constants.RANDOM, subDevSecret)));
    addTopo.setSub(topoSub);
    addTopo.setPk(DEV_PK);
    addTopo.setDevId(DEV_ID);
    mqttconnect.message.setPayload(JsonUtil.toBytes(addTopo));
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 设备上线 */
  @SneakyThrows
  public static void devLogin(String subDevPk, String subDevId) {
    DevLogin devLogin = new DevLogin();
    devLogin.setDevId(subDevId);
    devLogin.setPk(subDevPk);
    mqttconnect.message.setPayload(JsonUtil.toBytes(devLogin));
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 设备离线 */
  @SneakyThrows
  public static void devLogout(String subDevPk, String subDevId) {
    DevLogout devLogout = new DevLogout();
    devLogout.setDevId(subDevId);
    devLogout.setPk(subDevPk);
    mqttconnect.message.setPayload(JsonUtil.toBytes(devLogout));
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 获取拓扑关系 */
  @SneakyThrows
  public static void getTopo() {
    GetTopo getTopo = new GetTopo();
    getTopo.setPk(DEV_PK);
    getTopo.setDevId(DEV_ID);
    mqttconnect.message.setPayload(JsonUtil.toBytes(getTopo));
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 删除子设备拓扑关系 */
  @SneakyThrows
  public static void delDev(String subDevPk, String subDevId) {
    DelTopo delTopo = new DelTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(subDevPk);
    topoSub.setDevId(subDevId);
    delTopo.setSub(topoSub);
    delTopo.setPk(DEV_PK);
    delTopo.setDevId(DEV_ID);
    mqttconnect.message.setPayload(JsonUtil.toBytes(delTopo));
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }

  /** 设备发送数据 */
  @SneakyThrows
  public static void devSend(String kLink) {
    mqttconnect.message.setPayload(kLink.getBytes());
    mqttconnect.publish(mqttconnect.topic11, mqttconnect.message);
  }
}
