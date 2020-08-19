package hekr.me.iotos.softgateway.common.codec;

import cn.hutool.core.util.ArrayUtil;
import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import hekr.me.iotos.softgateway.common.constant.SubKlinkAction;
import hekr.me.iotos.softgateway.common.klink.BatchDevSend;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.common.klink.KlinkDev;
import hekr.me.iotos.softgateway.common.klink.SuModelData;
import hekr.me.iotos.softgateway.northProxy.device.Device;
import hekr.me.iotos.softgateway.northProxy.device.DeviceService;
import hekr.me.iotos.softgateway.pluginAsClient.tcp.packet.TcpPacket;
import hekr.me.iotos.softgateway.utils.ParseUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 此处为业务数据处理转换成klink标准协议格式的示例
 *
 * <p>用户可以参考此示例代码逻辑，实现DataCodec后自行处理
 */
@Slf4j
@Component
public class RawDataCodec implements DataCodec {

  @Autowired private ProxyConfig proxyConfig;
  @Autowired private DeviceService deviceService;

  private byte[] STATUS = {1, 2, 3};
  private byte[] ALARMS = {4, 5, 6, 9, 11, 12};

  @SneakyThrows
  @Override
  public KlinkDev decode(Object data) {
    TcpPacket tcpPacket = (TcpPacket) data;
    // 获取原始数据
    ByteBuffer wrap = ByteBuffer.wrap(tcpPacket.getBody());
    // 去除帧头部
    wrap.get();
    // 获取第一个byte，进行分别处理
    switch (wrap.get()) {
      case 1:
        byte length = wrap.get();
        byte[] macBytes = new byte[6];
        wrap.get(macBytes);
        byte deviceTypeByte = wrap.get();
        int areaSize = ParseUtil.byte2int(wrap.get());
        List<SuModelData> devSendList = new ArrayList<>();
        int i = 0;
        if (areaSize > 64) {
          i = 64;
        }
        for (; i < areaSize; i++) {
          Device device = deviceService.getByAreaNum(i + 1);
          byte status = wrap.get();
          if (device != null) {
            SuModelData suModelData = new SuModelData();
            suModelData.setPk(proxyConfig.getSUB_PK());
            suModelData.setCmd("report");
            suModelData.setDevId(device.getDevId());
            Map<String, Object> map = new HashMap<>();
            map.put("MAC", ParseUtil.parseByte2HexStr(macBytes));
            map.put("DTY", ParseUtil.byte2int(deviceTypeByte));
            map.put("CODE", i + 1);
            if (ArrayUtil.contains(STATUS, status)) {
              map.put("PRT", ParseUtil.byte2int(status));
            } else if (ArrayUtil.contains(ALARMS, status)) {
              map.put("ALM", ParseUtil.byte2int(status));
            }
            suModelData.setParams(map);
            devSendList.add(suModelData);
          }
        }
        BatchDevSend batchDevSend = new BatchDevSend();
        batchDevSend.setDevId(proxyConfig.getDEV_ID());
        batchDevSend.setPk(proxyConfig.getDEV_PK());
        batchDevSend.setData(devSendList);
        return batchDevSend;
        //      case 2:
        //        // 获取pk长度
        //        int pkLength = wrap.getShort();
        //        byte[] dstPk = new byte[pkLength];
        //        // 获取pk值
        //        wrap.get(dstPk);
        //        String pk = new String(dstPk, TcpPacket.CHARSET);
        //        // 获取devId长度
        //        short devIdLength = wrap.getShort();
        //        // 获取devId
        //        byte[] dstDevId = new byte[devIdLength];
        //        wrap.get(dstDevId);
        //        String devId = new String(dstDevId, TcpPacket.CHARSET);
        //
        //        short devSecretLength = wrap.getShort();
        //        byte[] dstDevSecret = new byte[devSecretLength];
        //        wrap.get(dstDevSecret);
        //        String devSecret = new String(dstDevSecret, TcpPacket.CHARSET);
        //
        //        // 构造上线指令
        //        DevSend klinkDev = new DevSend();
        //        klinkDev.setDevId(devId);
        //        klinkDev.setPk(pk);
        //        klinkDev.setMsgId(1);
        //        klinkDev.setDevSecret(devSecret);
        //        klinkDev.setAction(SubKlinkAction.DEV_LOGIN);
        //        return klinkDev;
      default:
        break;
    }
    return null;
  }

  @Override
  public Object encode(KlinkDev klink) {
    if (klink.getAction().equals(SubKlinkAction.HEARTBEAT)) {
      return new byte[] {3};
    }
    return null;
  }
}
