package me.hekr.iotos.softgateway.core.common.klink;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.config.GatewayConfig;
import me.hekr.iotos.softgateway.core.common.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.common.constant.Constants;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import me.hekr.iotos.softgateway.core.utils.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Title:Server 这是发送消息的服务端 配置数据平台相关参数，可执行设备上报数据至平台以及平台下发数据至设备 Description:
 * 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题 此为模拟烟雾传感器连接至智慧消防平台，并且其与IoTOS平台进行交互的代码
 * 添加设备和配置流程详情请见《设备连接软网关数据上报说明书》
 *
 * @author iotos
 */
@Slf4j
@Service
public class KlinkService {
  @Autowired private IotOsConfig iotOsConfig;
  @Autowired private MqttService mqttService;
  /** 动态注册设备 */
  @SneakyThrows
  public void register(String subDevPk, String subDevId, String devName) {
    register(subDevPk, subDevId, null, devName);
  }

  /** 动态注册设备 */
  @SneakyThrows
  public void register(String subDevPk, String subDevId, String productSecret, String devName) {
    doRegister(subDevPk, subDevId, productSecret, devName);
  }

  private void doRegister(String subDevPk, String subDevId, String productSecret, String devName)
      throws Exception {
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
    mqttService.publish(register);
  }

  /** 设备拓扑 */
  @SneakyThrows
  public void addDev(String subDevPk, String subDevId) {
    addDev(subDevPk, subDevId, null);
  }

  /** 设备拓扑 */
  @SneakyThrows
  public void addDev(String subDevPk, String subDevId, String subDevSecret) {
    doAddTopo(subDevPk, subDevId, subDevSecret);
  }

  private void doAddTopo(String subDevPk, String subDevId, String subDevSecret) throws Exception {
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
    GatewayConfig g = iotOsConfig.getGatewayConfig();
    addTopo.setPk(g.getPk());
    addTopo.setDevId(g.getDevId());
    mqttService.publish(addTopo);
  }

  /** 设备上线 */
  @SneakyThrows
  public void devLogin(String subDevPk, String subDevId) {
    doDevLogin(subDevPk, subDevId);
  }

  private void doDevLogin(String subDevPk, String subDevId) {
    DevLogin devLogin = new DevLogin();
    devLogin.setDevId(subDevId);
    devLogin.setPk(subDevPk);
    mqttService.publish(devLogin);
  }

  /** 设备离线 */
  @SneakyThrows
  public void devLogout(String subDevPk, String subDevId) {
    doDevLogout(subDevPk, subDevId);
  }

  private void doDevLogout(String subDevPk, String subDevId) {
    DevLogout devLogout = new DevLogout();
    devLogout.setDevId(subDevId);
    devLogout.setPk(subDevPk);
    mqttService.publish(devLogout);
  }

  /** 获取拓扑关系 */
  @SneakyThrows
  public void getTopo() {
    doGetTopo();
  }

  private void doGetTopo() {
    GetTopo getTopo = new GetTopo();
    getTopo.setPk(iotOsConfig.getGatewayConfig().getPk());
    getTopo.setDevId(iotOsConfig.getGatewayConfig().getDevId());
    mqttService.publish(getTopo);
  }

  /** 删除子设备拓扑关系 */
  @SneakyThrows
  public void delDev(String subDevPk, String subDevId) {
    DelTopo delTopo = new DelTopo();
    TopoSub topoSub = new TopoSub();
    topoSub.setPk(subDevPk);
    topoSub.setDevId(subDevId);
    delTopo.setSub(topoSub);
    GatewayConfig g = iotOsConfig.getGatewayConfig();
    delTopo.setPk(g.getPk());
    delTopo.setDevId(g.getDevId());
    mqttService.publish(delTopo);
  }

  /** 设备发送数据 */
  @SneakyThrows
  public void devSend(Object kLink) {
    mqttService.publish(kLink);
  }

  /** 获取远程配置文件 */
  @SneakyThrows
  public void getConfig() {
    GetConfig getConfig = new GetConfig();
    GatewayConfig g = iotOsConfig.getGatewayConfig();
    getConfig.setPk(g.getPk());
    getConfig.setDevId(g.getDevId());
    mqttService.publish(getConfig);
  }
}
