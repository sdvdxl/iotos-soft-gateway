package com.example.iotcloudgateway;

/**
 *  定义上报数据的方法
 */
public class SubKLink {
    // 向指定软网关绑定子设备
    public String addSub(String dev_pk, String dev_id, String subdev_pk, String subdev_id){
        return "{\"action\": \"addTopo\",\"msgId\": 1,\"pk\":\"" + dev_pk + "\",\"devId\": \"" + dev_id + "\",\"sub\": {\"pk\":\"" + subdev_pk + "\", \"devId\": \"" + subdev_id + "\"}}";
    }
    // 向指定软网关解绑子设备
    public String delSub(String dev_pk, String dev_id, String subdev_pk, String subdev_id){
        return "{\"action\": \"delTopo\",\"msgId\": 1,\"pk\":\"" + dev_pk + "\",\"devId\": \"" + dev_id + "\",\"sub\": {\"pk\":\"" + subdev_pk + "\", \"devId\": \"" + subdev_id + "\"}}";
    }
    // 查看指定软网关下设备绑定情况拓扑
    public String subTopo(String dev_pk, String dev_id){
        return "{\"action\": \"getTopo\",\"msgId\": 1,\"pk\": \"" + dev_pk + "\",\"devId\": \"" + dev_id + "\"}";
    }
    // 子设备上线
    public String subLogin(String subdev_pk, String subdev_id){
        return "{\"action\": \"devLogin\",\"msgId\": 1,\"pk\": \"" + subdev_pk + "\",\"devId\": \"" + subdev_id + "\"}";
    }
    // 子设备下线
    public String subLogout(String subdev_pk, String subdev_id){
        return "{\"action\": \"devLogout\",\"msgId\": 1,\"pk\": \"" + subdev_pk + "\",\"devId\": \"" + subdev_id + "\"}";
    }
}
