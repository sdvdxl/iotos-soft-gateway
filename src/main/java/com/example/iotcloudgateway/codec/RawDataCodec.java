package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.server.tcp.TcpPacket;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import java.nio.ByteBuffer;
import lombok.SneakyThrows;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;

/**
 * @author jiatao
 * @date 2020/6/22
 */
public class RawDataCodec implements DataCodec {

  @SneakyThrows
  @Override
  public KlinkDev decode(Object data, ChannelContext channelContext) {
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
        if (channelContext.userid == null) {
          Tio.bindUser(channelContext, pk + "@" + devId);
        }
        // 构造上线指令
        KlinkDev klinkDev = new KlinkDev();
        klinkDev.setDevId(devId);
        klinkDev.setPk(pk);
        klinkDev.setMsgId(1);
        klinkDev.setAction(SubKlinkAction.DEV_LOGIN);
        return klinkDev;
      case 2:
        break;
      case 3:
        KlinkDev heartBreak = new KlinkDev();
        heartBreak.setAction(SubKlinkAction.HEARTBEAT);
        return heartBreak;
      case 4:
        KlinkDev getTopo = new KlinkDev();
        getTopo.setAction(SubKlinkAction.GET_TOPO);
        return getTopo;
      default:
        break;
    }
    return null;
  }

  @Override
  public Object encode(KlinkDev klink, ChannelContext channelContext) {
    return null;
  }
}
