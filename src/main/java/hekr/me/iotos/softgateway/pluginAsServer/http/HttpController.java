package hekr.me.iotos.softgateway.pluginAsServer.http;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import com.sun.tools.javac.util.StringUtils;
import hekr.me.iotos.softgateway.common.codec.DataCodec;
import hekr.me.iotos.softgateway.common.codec.RawDataCodec;
import hekr.me.iotos.softgateway.common.constant.SubKlinkAction;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.common.exception.HttpResponseException;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.northProxy.ProxyService;
import hekr.me.iotos.softgateway.pluginAsServer.tcp.packet.TcpPacket;
import hekr.me.iotos.softgateway.utils.AESUtils;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

/** */
@Slf4j
@RequestPath(value = "/gateway")
public class HttpController {
  private DataCodec dataCodec = new RawDataCodec();

  public HttpController() {}

  /**
   * 测试示例接口
   *
   * <p>http设备访问此接口
   *
   * <p>此接口将获取到的数据经过DataCodec转换成klink格式后使用ProxyService向IoT OS上报数据
   */
  @RequestPath(value = "/push")
  public HttpResponse push(HttpRequest request) throws Exception {
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(request.getBody());
    DevSend klinkDev = dataCodec.decode(tcpPacket);
    if (klinkDev == null) {
      log.error("数据解码成klink格式失败：{}", tcpPacket.getBody());
      throw new RuntimeException();
    }

    // 对转码后的数据按照klink的action进行不同业务的操作
    ProxyService.sendKlink(klinkDev);

    HttpResponse ret = Resps.bytes(request, TcpPacket.HTTP_RESP, "ok");
    return ret;
  }

  @SneakyThrows
  @RequestPath(value = "/postInOutRecord")
  public HttpResponse postInOutRecord(HttpRequest request) {
    // 将数据解码后
    try {
//      EntranceReq entranceReq =
//          JsonUtil.fromBytes(AESUtils.decodeRequestData(request.getBody()), EntranceReq.class);
      EntranceReq entranceReq =
          JsonUtil.fromBytes(request.getBody(), EntranceReq.class);
      if (!checkEntranceReq(entranceReq)) {
        return Resps.json(request, new BaseResp(403, "缺少参数"));
      }
      return Resps.json(request, getSuccessResp());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Resps.resp500(request, e);
    }
  }

  private BaseResp getSuccessResp() {
    BaseResp baseResp = new BaseResp();
    baseResp.setResCode(0);
    baseResp.setResMsg("成功");
    return baseResp;
  }

  private boolean checkEntranceReq(EntranceReq entranceReq) {
    return checkNotNull(
        entranceReq.getCarCode(),
        entranceReq.getInTime(),
        entranceReq.getPassTime(),
        entranceReq.getParkID(),
        entranceReq.getInOrOut(),
        entranceReq.getGUID(),
        entranceReq.getChannelID());
  }

  private boolean checkNotNull(Object... objects) {
    for (Object object : objects) {
      if (object == null) {
        return false;
      }
    }
    return true;
  }
}
