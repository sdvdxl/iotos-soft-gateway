package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.server.tcp.TcpPacket;
import iot.cloud.os.core.api.dto.klink.DevSend;
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
        // 获取pk长度
        int firstFldLen = wrap.getShort();
        byte[] byteFirstFld = new byte[firstFldLen];
        // 获取pk值
        wrap.get(byteFirstFld);
        String firstFld = new String(byteFirstFld, TcpPacket.CHARSET);
        // 获取devId长度
        short secondFldLen = wrap.getShort();
        // 获取devId
        byte[] byteSecondFld = new byte[secondFldLen];
        wrap.get(byteFirstFld);
        String secondFld = new String(byteSecondFld, TcpPacket.CHARSET);
        // 获取data长度
        short thirdFldLen = wrap.getShort();
        // 获取data
        byte[] byteThirdFld = new byte[thirdFldLen];
        wrap.get(byteFirstFld);
        String thirdFld = new String(byteThirdFld, TcpPacket.CHARSET);
        //发送指令
        KlinkDev sendMsg = new KlinkDev();
        sendMsg.setDevId(secondFld);
        sendMsg.setPk(firstFld);
        sendMsg.setMsgId(1);
        sendMsg.setSysCustomRaw(thirdFld);
        sendMsg.setAction(SubKlinkAction.DEV_SEND);
        return sendMsg;
      case 3:
        KlinkDev heartBeat = new KlinkDev();
        heartBeat.setAction(SubKlinkAction.HEARTBEAT);
        return heartBeat;
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
