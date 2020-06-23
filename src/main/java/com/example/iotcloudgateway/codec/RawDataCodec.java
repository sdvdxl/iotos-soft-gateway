package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.dto.SubKlinkAction;
import com.example.iotcloudgateway.dto.TcpPacket;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import java.nio.ByteBuffer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author jiatao
 * @date 2020/6/22
 */
@Component
public class RawDataCodec implements DataCodec {

  @SneakyThrows
  @Override
  public KlinkDev decode(Object data) {
    TcpPacket tcpPacket = (TcpPacket) data;
    // 获取原始数据
    ByteBuffer wrap = ByteBuffer.wrap(tcpPacket.getBody());
    // 获取第一个byte，进行分别处理
    switch (wrap.get()) {
      case 1:
        // 获取pk长度
        int pkLength = wrap.getShort();
        byte[] dstPk = new byte[pkLength];
        // 获取pk值
        wrap.get(dstPk);
        String pk = new String(dstPk, TcpPacket.CHARSET);
        // 获取devId长度
        short devIdLength = wrap.getShort();
        // 获取devId
        byte[] dstDevId = new byte[devIdLength];
        wrap.get(dstDevId);
        String devId = new String(dstDevId, TcpPacket.CHARSET);

        // 构造上线指令
        KlinkDev klinkDev = new KlinkDev();
        klinkDev.setDevId(devId);
        klinkDev.setPk(pk);
        klinkDev.setMsgId(1);
        klinkDev.setAction(SubKlinkAction.DEV_LOGIN);
        return klinkDev;
      case 2:
        break;
      default:
        break;
    }
    return null;
  }

  @Override
  public Object encode(KlinkDev klink) {
    return null;
  }
}
