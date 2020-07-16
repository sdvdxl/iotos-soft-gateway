package hekr.me.iot.softgateway.northProxy;

import hekr.me.iot.softgateway.common.enums.Action;
import hekr.me.iot.softgateway.common.klink.AddTopo;
import hekr.me.iot.softgateway.common.klink.DelTopo;
import hekr.me.iot.softgateway.common.klink.DevLogin;
import hekr.me.iot.softgateway.common.klink.DevLogout;
import hekr.me.iot.softgateway.common.klink.GetConfig;
import hekr.me.iot.softgateway.common.klink.GetTopo;
import hekr.me.iot.softgateway.common.klink.KlinkDev;
import hekr.me.iot.softgateway.common.klink.Register;
import hekr.me.iot.softgateway.common.klink.TopoSub;
import hekr.me.iot.softgateway.common.constant.Constants;
import hekr.me.iot.softgateway.utils.ParseUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.tio.utils.jfinal.P;

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
  public static final String HOST = "tcp://" + P.get("mqtt.host");
  // 软网关的产品pk，进入产品中心-设备管理-智慧消防平台软网关，"产品pk"栏目即可查询
  public static final String DEV_PK = P.get("mqtt.gateway.DEV_PK");
  // 软网关的设备id，进入产品中心-设备管理-智慧消防平台软网关，"产品id"栏目即可查询
  public static final String DEV_ID = P.get("mqtt.gateway.DEV_ID");

  public static final String DEV_SECRET = P.get("mqtt.gateway.devSecret");

  // 接入至IoTOS的账号密码，生成方式详情请见《设备连接软网关数据上报说明书》
  public static final String userName = Constants.HASH_METHOD + ":" + Constants.RANDOM;

  private static MqttConnectService mqttConnect;

  public static void init() throws MqttException {
    mqttConnect = new MqttConnectService();
  }

  @SneakyThrows
  public static String getPassword() {
    return ParseUtil.parseByte2HexStr(
        (ParseUtil.HmacSHA1Encrypt(DEV_PK + DEV_ID + DEV_SECRET + Constants.RANDOM, DEV_SECRET)));
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
        devSend(klinkDev);
        break;
      case HEARTBEAT:
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + klinkDev.getAction());
    }
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
    mqttConnect.publish(register);
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
    mqttConnect.publish(addTopo);
  }

  /** 设备上线 */
  @SneakyThrows
  public static void devLogin(String subDevPk, String subDevId) {
    DevLogin devLogin = new DevLogin();
    devLogin.setDevId(subDevId);
    devLogin.setPk(subDevPk);
    mqttConnect.publish(devLogin);
  }

  /** 设备离线 */
  @SneakyThrows
  public static void devLogout(String subDevPk, String subDevId) {
    DevLogout devLogout = new DevLogout();
    devLogout.setDevId(subDevId);
    devLogout.setPk(subDevPk);
    mqttConnect.publish(devLogout);
  }

  /** 获取拓扑关系 */
  @SneakyThrows
  public static void getTopo() {
    GetTopo getTopo = new GetTopo();
    getTopo.setPk(DEV_PK);
    getTopo.setDevId(DEV_ID);
    mqttConnect.publish(getTopo);
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
    mqttConnect.publish(delTopo);
  }

  /** 设备发送数据 */
  @SneakyThrows
  public static void devSend(Object kLink) {
    mqttConnect.publish(kLink);
  }

  /** 获取远程配置文件 */
  @SneakyThrows
  public void getConfig() {
    GetConfig getConfig = new GetConfig();
    getConfig.setPk(DEV_PK);
    getConfig.setDevId(DEV_ID);
    mqttConnect.publish(getConfig);
  }
}
