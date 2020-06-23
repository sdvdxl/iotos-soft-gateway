package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.tcp.TcpPacket;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** 字符串转换为klink格式 */
@Slf4j
//@Component
public class KlinkDataCodec implements DataCodec {

  @SneakyThrows
  @Override
  public KlinkDev decode(Object data) {
    TcpPacket tcpPacket = (TcpPacket) data;
    String str = new String(tcpPacket.getBody(), TcpPacket.CHARSET);

    KlinkDev klinkDev = null;
    try {
      klinkDev = JsonUtil.fromJson(str, KlinkDev.class);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return klinkDev;
  }

  @Override
  public Object encode(KlinkDev klink) {
    return null;
  }
}
