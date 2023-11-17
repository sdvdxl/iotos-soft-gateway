package me.hekr.iotos.softgateway.core.constant;

/**
 * <p>SubKlinkAction class.</p>
 *
 * @author du
 * @version $Id: $Id
 */
public class SubKlinkAction {
  /** 设备上报-注册 */
  public static final String REGISTER = "register";
  /** 设备上报-登录 */
  public static final String DEV_LOGIN = "devLogin";
  /** 设备上报-注册 */
  public static final String DEV_LOGOUT = "devLogout";
  /** 设备上报-添加topo */
  public static final String ADD_TOPO = "addTopo";
  /** 设备上报-上报数据 */
  public static final String DEV_SEND = "devSend";
  /** 设备上报-获取topo关系 */
  public static final String GET_TOPO = "getTopo";
  /** 设备上报-解除topo关系 */
  public static final String DEL_TOPO = "delTopo";

  /** 心跳 */
  public static final String HEARTBEAT = "heartbeat";

  /** 平台下发-登录回复 */
  public static final String DEV_LOGIN_RESP = "devLoginResp";

  /** Constant <code>CLOUD_SEND="cloudSend"</code> */
  public static final String CLOUD_SEND = "cloudSend";

  /** 平台下发-注册回复 */
  public static final String REGISTER_RESP = "registerResp";
  /** 平台下发-登出回复 */
  public static final String DEV_LOGOUT_RESP = "devLogoutResp";
  /** 平台下发-增加拓扑关系回复 */
  public static final String ADD_TOPO_RESP = "addTopoResp";
  /** 平台下发-设备上报回复 */
  public static final String DEV_SEND_RESP = "devSendResp";
  /** 平台下发-获取拓扑关系回复 */
  public static final String GET_TOPO_RESP = "getTopoResp";
  /** 平台下发-删除拓扑回复 */
  public static final String DEL_TOPO_RESP = "delTopoResp";
}
