package hekr.me.iotos.softgateway.northProxy;

import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.AddTopo;
import hekr.me.iotos.softgateway.common.klink.DelTopo;
import hekr.me.iotos.softgateway.common.klink.DevLogin;
import hekr.me.iotos.softgateway.common.klink.DevLogout;
import hekr.me.iotos.softgateway.common.klink.GetConfig;
import hekr.me.iotos.softgateway.common.klink.GetTopo;
import hekr.me.iotos.softgateway.common.klink.KlinkDev;
import hekr.me.iotos.softgateway.common.klink.Register;
import hekr.me.iotos.softgateway.common.klink.TopoSub;
import hekr.me.iotos.softgateway.common.constant.Constants;
import hekr.me.iotos.softgateway.utils.ParseUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.utils.jfinal.P;

/**
 * Title:Server 这是发送消息的服务端 配置数据平台相关参数，可执行设备上报数据至平台以及平台下发数据至设备 Description:
 * 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题 此为模拟烟雾传感器连接至智慧消防平台，并且其与IoTOS平台进行交互的代码
 * 添加设备和配置流程详情请见《设备连接软网关数据上报说明书》
 */
@Slf4j
@Service
public class ProxyService {
  @Autowired private ProxyConfig proxyConfig;
  @Autowired private ProxyConnectService proxyConnectService;

  public void sendKlink(KlinkDev klinkDev) {
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
  public void register(String subDevPk, String subDevId, String productSecret, String devName) {
    Register register = new Register();
    register.setDevId(subDevId);
    register.setPk(subDevPk);
    register.setName(devName);
    if (productSecret != null) {
      register.setRandom(Constants.RANDOM);
      register.setHashMethod(Constants.HASH_METHOD);
      register.setSign(
          ParseUtil.parseByte2HexStr(
              ParseUtil.HmacSHA1Encrypt(
                  subDevPk + productSecret + Constants.RANDOM, productSecret)));
    }
    proxyConnectService.publish(register);
  }

  /** 设备拓扑 */
  @SneakyThrows
  public void addDev(String subDevPk, String subDevId, String subDevSecret) {
    AddTopo addTopo = new AddTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(subDevPk);
    topoSub.setDevId(subDevId);
    if (subDevSecret != null) {
      topoSub.setHashMethod(Constants.HASH_METHOD);
      topoSub.setRandom(Constants.RANDOM);
      topoSub.setSign(
          ParseUtil.parseByte2HexStr(
              ParseUtil.HmacSHA1Encrypt(
                  subDevPk + subDevId + subDevSecret + Constants.RANDOM, subDevSecret)));
    }
    addTopo.setSub(topoSub);
    addTopo.setPk(proxyConfig.getDEV_PK());
    addTopo.setDevId(proxyConfig.getDEV_ID());
    proxyConnectService.publish(addTopo);
  }

  /** 设备上线 */
  @SneakyThrows
  public void devLogin(String subDevPk, String subDevId) {
    DevLogin devLogin = new DevLogin();
    devLogin.setDevId(subDevId);
    devLogin.setPk(subDevPk);
    proxyConnectService.publish(devLogin);
  }

  /** 设备离线 */
  @SneakyThrows
  public void devLogout(String subDevPk, String subDevId) {
    DevLogout devLogout = new DevLogout();
    devLogout.setDevId(subDevId);
    devLogout.setPk(subDevPk);
    proxyConnectService.publish(devLogout);
  }

  /** 获取拓扑关系 */
  @SneakyThrows
  public void getTopo() {
    GetTopo getTopo = new GetTopo();
    getTopo.setPk(proxyConfig.getDEV_PK());
    getTopo.setDevId(proxyConfig.getDEV_ID());
    proxyConnectService.publish(getTopo);
  }

  /** 删除子设备拓扑关系 */
  @SneakyThrows
  public void delDev(String subDevPk, String subDevId) {
    DelTopo delTopo = new DelTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(subDevPk);
    topoSub.setDevId(subDevId);
    delTopo.setSub(topoSub);
    delTopo.setPk(proxyConfig.getDEV_PK());
    delTopo.setDevId(proxyConfig.getDEV_ID());
    proxyConnectService.publish(delTopo);
  }

  /** 设备发送数据 */
  @SneakyThrows
  public void devSend(Object kLink) {
    proxyConnectService.publish(kLink);
  }

  /** 获取远程配置文件 */
  @SneakyThrows
  public void getConfig() {
    GetConfig getConfig = new GetConfig();
    getConfig.setPk(proxyConfig.getDEV_PK());
    getConfig.setDevId(proxyConfig.getDEV_ID());
    proxyConnectService.publish(getConfig);
  }
}
