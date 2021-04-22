package me.hekr.iotos.softgateway.common.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import me.hekr.iotos.softgateway.common.klink.AddTopo;
import me.hekr.iotos.softgateway.common.klink.BatchDevSend;
import me.hekr.iotos.softgateway.common.klink.CloudSend;
import me.hekr.iotos.softgateway.common.klink.DelTopo;
import me.hekr.iotos.softgateway.common.klink.DevLogin;
import me.hekr.iotos.softgateway.common.klink.DevLogout;
import me.hekr.iotos.softgateway.common.klink.DevSend;
import me.hekr.iotos.softgateway.common.klink.DevUpgrade;
import me.hekr.iotos.softgateway.common.klink.GetConfig;
import me.hekr.iotos.softgateway.common.klink.GetConfigResp;
import me.hekr.iotos.softgateway.common.klink.GetTopo;
import me.hekr.iotos.softgateway.common.klink.GetTopoResp;
import me.hekr.iotos.softgateway.common.klink.Klink;
import me.hekr.iotos.softgateway.common.klink.KlinkResp;
import me.hekr.iotos.softgateway.common.klink.NotSupport;
import me.hekr.iotos.softgateway.common.klink.Register;
import me.hekr.iotos.softgateway.common.klink.RegisterResp;

public enum Action {

  /** 不支持 */
  NOT_SUPPORT("_notSupport_"),

  /** 系统内部动作，mqtt 订阅事件 */
  INNER_MQTT_SUB("innerMqttSub", FrameType.INNER),

  /** 系统内部动作，mqtt 发布事件 */
  INNER_MQTT_PUB("innerMqttPub", FrameType.INNER),

  /** 网关上线，用于子设备推送 */
  GATEWAY_LOGIN("gatewayLogin"),

  /** 网关离线，用于子设备推送 */
  GATEWAY_LOGOUT("gatewayLogout"),

  /** 快照数据发生变化 */
  DATA_CHANGED("dataChanged"),

  /**
   * app 发送
   *
   * @see #CLOUD_SEND_RESP
   */
  CLOUD_SEND("cloudSend", FrameType.DEV_DOWN, CloudSend.class),
  /** 控制结果返回 注意：这个是从设备端返回的 */
  CLOUD_SEND_RESP("cloudSendResp", FrameType.DEV_UP, KlinkResp.class),

  /** 心跳 */
  HEARTBEAT("heartbeat", FrameType.DEV_UP, DevSend.class),

  /** 设备发送 */
  DEV_SEND("devSend", FrameType.DEV_UP, DevSend.class),
  /** 设备发送结果回复 */
  DEV_SEND_RESP("devSendResp", FrameType.DEV_DOWN),

  /** 设备批量发送 */
  BATCH_DEV_SEND("batchDevSend", FrameType.DEV_UP, BatchDevSend.class),
  /** 设备批量发送结果回复 */
  BATCH_DEV_SEND_RESP("batchDevSendResp", FrameType.DEV_DOWN),

  /** 设备登录 */
  DEV_LOGIN("devLogin", FrameType.DEV_UP, DevLogin.class),
  /** 设备登录回复 */
  DEV_LOGIN_RESP("devLoginResp", FrameType.DEV_DOWN),

  /** 设备登出 */
  DEV_LOGOUT("devLogout", FrameType.DEV_UP, DevLogout.class),
  /** 设备登出回复 */
  DEV_LOGOUT_RESP("devLogoutResp", FrameType.DEV_DOWN),

  /** 添加拓扑关系 */
  ADD_TOPO("addTopo", FrameType.DEV_UP, AddTopo.class),
  /** 添加拓扑关系回复 */
  ADD_TOPO_RESP("addTopoResp", FrameType.DEV_DOWN),

  /** 获取拓扑关系 */
  GET_TOPO("getTopo", FrameType.DEV_UP, GetTopo.class),
  /** 获取拓扑关系回复 */
  GET_TOPO_RESP("getTopoResp", FrameType.DEV_DOWN, GetTopoResp.class),

  /** 添加拓扑关系 */
  DEL_TOPO("delTopo", FrameType.DEV_UP, DelTopo.class),
  /** 添加拓扑关系回复 */
  DEL_TOPO_RESP("delTopoResp", FrameType.DEV_DOWN),

  /** 注册设备 */
  REGISTER("register", FrameType.DEV_UP, Register.class),
  /** 注册设备回复 */
  REGISTER_RESP("registerResp", FrameType.DEV_DOWN, RegisterResp.class),

  /** 控制设备固件升级 */
  DEV_UPGRADE("devUpgrade", FrameType.DEV_DOWN, DevUpgrade.class),

  /** 获取远程配置信息 */
  GET_CONFIG("getConfig", FrameType.DEV_UP, GetConfig.class),

  /** 获取远程配置信息回复 */
  GET_CONFIG_RESP("getConfigResp", FrameType.DEV_DOWN, GetConfigResp.class);

  public static final String ACTION_NAME = "action";
  private static final Map<String, Action> ACTION_MAP =
      Arrays.stream(Action.values()).collect(toMap(Action::getAction, Function.identity()));
  @Getter private final String action;
  @Getter private final Class<? extends Klink> klinkClass;
  @Getter private final FrameType frameType;

  Action(String action, FrameType frameType, Class<? extends Klink> klinkClass) {
    this.action = action;
    this.frameType = frameType;
    this.klinkClass = klinkClass;
  }

  Action(String action) {
    this(action, null, NotSupport.class);
  }

  Action(String action, FrameType frameType) {
    this(action, frameType, NotSupport.class);
  }

  /**
   * @param action ，etcd：devSend
   * @return 找不到对应的返回 NOT_SUPPORT
   */
  public static Action of(String action) {
    return ACTION_MAP.getOrDefault(action, NOT_SUPPORT);
  }

  /**
   * 返回相对应的action
   *
   * @return
   */
  public Action getPair() {
    if (isResp()) {
      return of(action.substring(0, this.getAction().indexOf("Resp")));
    }

    if (isSend()) {
      return of(action + "Resp");
    }

    return this;
  }

  public boolean isResp() {
    return this.action.endsWith("Resp");
  }

  public boolean isSend() {
    return this != NOT_SUPPORT && !this.isResp();
  }
}
