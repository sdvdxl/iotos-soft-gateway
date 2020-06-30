package com.example.iotcloudgateway.codec;

import com.example.iotcloudgateway.constant.SubKlinkAction;
import com.example.iotcloudgateway.klink.DevSend;
import com.example.iotcloudgateway.klink.ModelData;
import com.example.iotcloudgateway.server.tcp.TcpPacket;
import com.example.iotcloudgateway.utils.JsonUtil;
import java.nio.ByteBuffer;
import lombok.SneakyThrows;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;

/**
 * 此处为业务数据处理转换成klink标准协议格式的示例
 *
 * <p>用户可以参考此示例代码逻辑，实现DataCodec后自行处理
 */
public class RawDataCodec implements DataCodec {

  @SneakyThrows
  @Override
  public DevSend decode(Object data, ChannelContext channelContext) {
    TcpPacket tcpPacket = (TcpPacket) data;
    // 获取原始数据
    ByteBuffer wrap = ByteBuffer.wrap(tcpPacket.getBody());
    // 获取第一个byte，进行分别处理
    switch (wrap.get()) {
      case 0:
        // 获取pk长度
        int pkLength0 = wrap.getShort();
        byte[] dstPk0 = new byte[pkLength0];
        // 获取pk值
        wrap.get(dstPk0);
        String pk0 = new String(dstPk0, TcpPacket.CHARSET);
        // 获取devId长度
        short devIdLength0 = wrap.getShort();
        // 获取devId
        byte[] dstDevId0 = new byte[devIdLength0];
        wrap.get(dstDevId0);
        String devId0 = new String(dstDevId0, TcpPacket.CHARSET);

        short productSecretLength = wrap.getShort();
        byte[] dstProductSecret = new byte[productSecretLength];
        wrap.get(dstProductSecret);
        String productSecret = new String(dstProductSecret, TcpPacket.CHARSET);

        short nameLength = wrap.getShort();
        byte[] dstName = new byte[nameLength];
        wrap.get(dstName);
        String name = new String(dstName, TcpPacket.CHARSET);

        // 注意：此处为tcp区分通道的建议方式，以pk@devId的形式标记不同通道的id，以方便tcp传送数据给制定的设备
        if (channelContext != null && channelContext.userid == null) {
          Tio.bindUser(channelContext, pk0 + "@" + devId0);
        }

        // 构造上线指令
        DevSend klinkRegister = new DevSend();
        klinkRegister.setDevId(devId0);
        klinkRegister.setPk(pk0);
        klinkRegister.setMsgId(1);
        klinkRegister.setProductSecret(productSecret);
        klinkRegister.setAction(SubKlinkAction.REGISTER);
        klinkRegister.setName(name);
        return klinkRegister;
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

        short devSecretLength = wrap.getShort();
        byte[] dstDevSecret = new byte[devSecretLength];
        wrap.get(dstDevSecret);
        String devSecret = new String(dstDevSecret, TcpPacket.CHARSET);

        // 注意：此处为tcp区分通道的建议方式，以pk@devId的形式标记不同通道的id，以方便tcp传送数据给制定的设备
        if (channelContext != null && channelContext.userid == null) {
          Tio.bindUser(channelContext, pk + "@" + devId);
        }

        // 构造上线指令
        DevSend klinkDev = new DevSend();
        klinkDev.setDevId(devId);
        klinkDev.setPk(pk);
        klinkDev.setMsgId(1);
        klinkDev.setDevSecret(devSecret);
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
        wrap.get(byteSecondFld);
        String secondFld = new String(byteSecondFld, TcpPacket.CHARSET);
        // 获取data长度
        short thirdFldLen = wrap.getShort();
        // 获取data
        byte[] byteThirdFld = new byte[thirdFldLen];
        wrap.get(byteThirdFld);
        String thirdFld = new String(byteThirdFld, TcpPacket.CHARSET);
        // 发送指令
        DevSend sendMsg = new DevSend();
        sendMsg.setDevId(secondFld);
        sendMsg.setPk(firstFld);
        sendMsg.setMsgId(1);
        sendMsg.setData(JsonUtil.fromJson(thirdFld, ModelData.class));
        sendMsg.setAction(SubKlinkAction.DEV_SEND);
        return sendMsg;
      case 3:
        DevSend heartBeat = new DevSend();
        heartBeat.setAction(SubKlinkAction.HEARTBEAT);
        return heartBeat;
      case 4:
        DevSend getTopo = new DevSend();
        getTopo.setAction(SubKlinkAction.GET_TOPO);
        return getTopo;
      default:
        break;
    }
    return null;
  }

  @Override
  public Object encode(DevSend klink, ChannelContext channelContext) {
    return null;
  }
}
