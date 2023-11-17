package me.hekr.iotos.softgateway.core.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import lombok.ToString;
import me.hekr.iotos.softgateway.core.klink.AddTopo;
import me.hekr.iotos.softgateway.core.klink.AddTopoResp;
import me.hekr.iotos.softgateway.core.klink.BatchDevSend;
import me.hekr.iotos.softgateway.core.klink.CloudSend;
import me.hekr.iotos.softgateway.core.klink.DelTopo;
import me.hekr.iotos.softgateway.core.klink.DelTopoResp;
import me.hekr.iotos.softgateway.core.klink.DevLogin;
import me.hekr.iotos.softgateway.core.klink.DevLoginResp;
import me.hekr.iotos.softgateway.core.klink.DevLogout;
import me.hekr.iotos.softgateway.core.klink.DevLogoutResp;
import me.hekr.iotos.softgateway.core.klink.DevSend;
import me.hekr.iotos.softgateway.core.klink.DevSendResp;
import me.hekr.iotos.softgateway.core.klink.DevUpgrade;
import me.hekr.iotos.softgateway.core.klink.GetConfig;
import me.hekr.iotos.softgateway.core.klink.GetConfigResp;
import me.hekr.iotos.softgateway.core.klink.GetTopo;
import me.hekr.iotos.softgateway.core.klink.GetTopoResp;
import me.hekr.iotos.softgateway.core.klink.Klink;
import me.hekr.iotos.softgateway.core.klink.KlinkResp;
import me.hekr.iotos.softgateway.core.klink.NotSupport;
import me.hekr.iotos.softgateway.core.klink.Register;
import me.hekr.iotos.softgateway.core.klink.RegisterResp;

/**
 * <p>Action class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
@ToString(of = "action")
public enum Action {

  /** 不支持 */
  NOT_SUPPORT("_notSupport_"),

  /**
   * app 发送
   *
   * @see #CLOUD_SEND_RESP
   */
  CLOUD_SEND("cloudSend", FrameType.DEV_DOWN, CloudSend.class),
  /** 控制结果返回 注意：这个是从设备端返回的 */
  CLOUD_SEND_RESP("cloudSendResp", FrameType.DEV_UP, KlinkResp.class),

  /** 心跳 */
  HEARTBEAT("heartbeat", FrameType.DEV_UP, Klink.class),
  HEARTBEAT_RESP("heartbeatResp", FrameType.DEV_DOWN, KlinkResp.class),

  /** 设备发送 */
  DEV_SEND("devSend", FrameType.DEV_UP, DevSend.class),
  /** 设备发送结果回复 */
  DEV_SEND_RESP("devSendResp", FrameType.DEV_DOWN, DevSendResp.class),

  /** 设备批量发送 */
  BATCH_DEV_SEND("batchDevSend", FrameType.DEV_UP, BatchDevSend.class),
  /** 设备批量发送结果回复 */
  BATCH_DEV_SEND_RESP("batchDevSendResp", FrameType.DEV_DOWN, KlinkResp.class),

  /** 设备登录 */
  DEV_LOGIN("devLogin", FrameType.DEV_UP, DevLogin.class),
  /** 设备登录回复 */
  DEV_LOGIN_RESP("devLoginResp", FrameType.DEV_DOWN, DevLoginResp.class),

  /** 设备登出 */
  DEV_LOGOUT("devLogout", FrameType.DEV_UP, DevLogout.class),
  /** 设备登出回复 */
  DEV_LOGOUT_RESP("devLogoutResp", FrameType.DEV_DOWN, DevLogoutResp.class),

  /** 添加拓扑关系 */
  ADD_TOPO("addTopo", FrameType.DEV_UP, AddTopo.class),
  /** 添加拓扑关系回复 */
  ADD_TOPO_RESP("addTopoResp", FrameType.DEV_DOWN, AddTopoResp.class),

  /** 获取拓扑关系 */
  GET_TOPO("getTopo", FrameType.DEV_UP, GetTopo.class),
  /** 获取拓扑关系回复 */
  GET_TOPO_RESP("getTopoResp", FrameType.DEV_DOWN, GetTopoResp.class),

  /** 添加拓扑关系 */
  DEL_TOPO("delTopo", FrameType.DEV_UP, DelTopo.class),
  /** 添加拓扑关系回复 */
  DEL_TOPO_RESP("delTopoResp", FrameType.DEV_DOWN, DelTopoResp.class),

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

  /** Constant <code>ACTION_NAME="action"</code> */
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
   * <p>of.</p>
   *
   * @param action ，etcd：devSend
   * @return 找不到对应的返回 NOT_SUPPORT
   */
  public static Action of(String action) {
    return ACTION_MAP.getOrDefault(action, NOT_SUPPORT);
  }

  /**
   * 返回相对应的action
   *
   * @return 返回对应的配对，如果是请求，则返回 Resp；如果是 Resp，则返回请求。找不到返回自身
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

  /**
   * <p>isResp.</p>
   *
   * @return a boolean.
   */
  public boolean isResp() {
    return this.action.endsWith("Resp");
  }

  /**
   * <p>isSend.</p>
   *
   * @return a boolean.
   */
  public boolean isSend() {
    return this != NOT_SUPPORT && !this.isResp();
  }
}
