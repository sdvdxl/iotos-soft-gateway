package com.example.iotcloudgateway.Handler;

import com.example.iotcloudgateway.Codec.LinePacketCodec;
import com.example.iotcloudgateway.SubMqtt.MqttServer;
import com.example.iotcloudgateway.dto.TcpPacket;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.DevLoginReq;
import iot.cloud.os.core.api.dto.klink.DevLoginResp;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import iot.cloud.os.core.api.enums.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

@TioServerMsgHandler
@Slf4j
public class TcpServerMsgHandler implements ServerAioHandler {
    @Autowired private LinePacketCodec packetCodec;
//    @Autowired private MqttServer mqttServer;

    @Override
    public Packet decode(
            ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
            throws AioDecodeException {
        return packetCodec.decode(buffer, limit, position, readableLength, channelContext);
    }

    @Override
    public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
        return packetCodec.encode(packet, tioConfig, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        TcpPacket tcpPacket = (TcpPacket) packet;
        if (((TcpPacket) packet).getBody() == null) {
            // 解码时因特殊原因丢弃此帧数据，故此处不做处理
            return;
        }
        String str = new String(tcpPacket.getBody(), TcpPacket.CHARSET);

        KlinkDev klinkDev = null;
        try {
            klinkDev = JsonUtil.fromJson(str, KlinkDev.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        // 若已进行登录校验过，直接将数据传到core
        if (channelContext.userid != null) {
            // klink格式时检测是否为心跳包，如是则不发送到core
            // 包含{"action":"heartbeat"}则为心跳包
            if (klinkDev != null && klinkDev.getAction().equals("heartbeat")) {
                return;
            }
//            TransferPacket transferPacket = new TransferPacket();
//            transferPacket.setFinger(redisStreamService.getStreamFinger());
//            transferPacket.setSessionId(channelContext.getId());
//            Device deviceInfo = new Device(channelContext.userid);
//            transferPacket.setPk(deviceInfo.getPk());
//            transferPacket.setDevId(deviceInfo.getDevId());
//            transferPacket.setSysMsgId(StringUtil.trimUuid());
//            transferPacket.setTimestamp(System.currentTimeMillis());
//            transferPacket.setEncoded(false);
//            transferPacket.setIp(channelContext.getClientNode().getIp());
//            transferPacket.setPayload(Base64.encodeBase64String(tcpPacket.getBody()));
//            redisStreamService.publishToCore(transferPacket);
//            switch (klinkDev.getAction()){
//                case "devLogin":
//                    mqttServer.devLogin(klinkDev.getPk(),klinkDev.getDevId());
//                    break;
//            }
//            return;
        }
//
//        // 若未进行过登录校验，则进行登录校验
//        if (klinkDev != null) {
//            // 登录校验必须为klink格式
//            if (Action.of(klinkDev.getAction()) == Action.DEV_LOGIN) {
//                DevLoginReq devLoginReq = new DevLoginReq();
//                devLoginReq.setFinger(redisStreamService.getStreamFinger());
//                devLoginReq.setSessionId(channelContext.getId());
//                devLoginReq.setPk(klinkDev.getPk());
//                devLoginReq.setDevId(klinkDev.getDevId());
//                devLoginReq.setIp(channelContext.getClientNode().getIp());
//                DevLoginResp devLoginResp = deviceClient.devLogin(devLoginReq);
//                if (devLoginResp.isSuccess()) {
//                    log.debug(
//                            "device=> devId:{}, pk:{}, login success",
//                            devLoginResp.getDevId(),
//                            devLoginResp.getPk());
//                    // 登录校验成功后对链接进行绑定，将userId以"{pk}@{devId}"形式命名
//                    Tio.bindUser(channelContext, klinkDev.getPk() + "@" + klinkDev.getDevId());
//                } else {
//                    log.warn(
//                            "device=> devId:{}, pk:{}, login fail",
//                            devLoginResp.getDevId(),
//                            devLoginResp.getPk());
//                }
//                TcpPacket respPacket = new TcpPacket();
//                respPacket.setBody(JsonUtil.toBytes(devLoginResp));
//                Tio.send(channelContext, respPacket);
//            }
//        }
    }
}
