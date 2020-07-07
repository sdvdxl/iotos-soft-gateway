package hekr.me.iotcloudgateway.constant;

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

  public static final String REGISTER_RESP = "registerResp";

  public static final String DEV_LOGOUT_RESP = "devLogoutResp";
  public static final String ADD_TOPO_RESP = "addTopoResp";
  public static final String DEV_SEND_RESP = "devSendResp";
  public static final String GET_TOPO_RESP = "getTopoResp";
  public static final String DEL_TOPO_RESP = "delTopoResp";
}
