package com.example.iotcloudgateway.subMqtt;

/** 定义上报数据的方法 */
public class SubKLink {
  // 向指定软网关绑定子设备
  public static String addSub(String devPk, String devId, String subDevPk, String subDevId) {
    return "{\"action\": \"addTopo\",\"msgId\": 1,\"pk\":\""
        + devPk
        + "\",\"devId\": \""
        + devId
        + "\",\"sub\": {\"pk\":\""
        + subDevPk
        + "\", \"devId\": \""
        + subDevId
        + "\"}}";
  }
  // 向指定软网关解绑子设备
  public static String delSub(String devPk, String devId, String subDevPk, String subDevId) {
    return "{\"action\": \"delTopo\",\"msgId\": 1,\"pk\":\""
        + devPk
        + "\",\"devId\": \""
        + devId
        + "\",\"sub\": {\"pk\":\""
        + subDevPk
        + "\", \"devId\": \""
        + subDevId
        + "\"}}";
  }
  // 查看指定软网关下设备绑定情况拓扑
  public static String subTopo(String devPk, String devId) {
    return "{\"action\": \"getTopo\",\"msgId\": 1,\"pk\": \""
        + devPk
        + "\",\"devId\": \""
        + devId
        + "\"}";
  }
  // 子设备上线
  public static String subLogin(String subDevPk, String subDevId) {
    return "{\"action\": \"devLogin\",\"msgId\": 1,\"pk\": \""
        + subDevPk
        + "\",\"devId\": \""
        + subDevId
        + "\"}";
  }
  // 子设备下线
  public static String subLogout(String subDevPk, String subDevId) {
    return "{\"action\": \"devLogout\",\"msgId\": 1,\"pk\": \""
        + subDevPk
        + "\",\"devId\": \""
        + subDevId
        + "\"}";
  }
}
