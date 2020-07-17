package hekr.me.iotos.softgateway.pluginAsClient.http;

import hekr.me.iotos.softgateway.common.codec.DataCodec;
import hekr.me.iotos.softgateway.common.codec.RawDataCodec;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.pluginAsServer.tcp.packet.TcpPacket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tio.utils.jfinal.P;

/**
 * 此类为http协议客户端代码示例
 *
 * <p>用户可以根据对接的http server接口进行相应的代码编写
 *
 * <p>如下example()方法
 */
@Slf4j
public class HttpClient {
  private static String baseUrl = "http://" + P.get("http.client.connect.host");

  /**
   * 此接口调用的是本项目中HttpController对应示例接口
   *
   * <p>此处模拟网关以客户端身份去获取"http server形式设备"的数据，因此要将获取到的数据调用Datacodec:decode方法来解析
   */
  public static void example() {
    // 此处填写访问的路径
    String url = baseUrl + "/gateway/test";
    HttpUtils httpUtils = new HttpUtils();
    byte[] response = httpUtils.get(url, null, null);
    TcpPacket tcpPacket = new TcpPacket();
    tcpPacket.setBody(response);
    DataCodec dataCodec = new RawDataCodec();
    // 将获取到的信息进行解码
    DevSend decode = dataCodec.decode(tcpPacket);
    System.out.println(decode);
  }
}
