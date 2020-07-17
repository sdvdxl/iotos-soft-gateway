package hekr.me.iotos.softgateway.common.codec;

import hekr.me.iotos.softgateway.common.constant.SubKlinkAction;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.common.klink.ModelData;
import hekr.me.iotos.softgateway.pluginAsServer.tcp.packet.TcpPacket;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import java.nio.ByteBuffer;
import lombok.SneakyThrows;

/**
 * 此处为业务数据处理转换成klink标准协议格式的示例
 *
 * <p>用户可以参考此示例代码逻辑，实现DataCodec后自行处理
 */
public class RawDataCodec implements DataCodec {

  @SneakyThrows
  @Override
  public DevSend decode(Object data) {
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
  public Object encode(DevSend klink) {
    if (klink.getAction().equals(SubKlinkAction.HEARTBEAT)){
      return new byte[]{3};
    }
    return null;
  }
}