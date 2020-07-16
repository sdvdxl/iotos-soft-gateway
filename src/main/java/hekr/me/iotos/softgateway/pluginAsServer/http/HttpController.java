package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.codec.DataCodec;
import hekr.me.iotos.softgateway.common.codec.RawDataCodec;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.northProxy.ProxyService;
import hekr.me.iotos.softgateway.pluginAsServer.tcp.TcpPacket;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

/**
 * http 接口类
 *
 * <p>开发者可以在此处编写符合对接设备的接口，下面为两个接口示例
 */
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

  @RequestPath(value = "/test")
  public HttpResponse sendCommand(HttpRequest request) throws Exception {
    HttpResponse ret = Resps.bytes(request, JsonUtil.toBytes("test ok"), "ok");
    return ret;
  }
}
