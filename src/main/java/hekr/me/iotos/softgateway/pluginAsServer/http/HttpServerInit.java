package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.IoTGatewayApplication;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.utils.jfinal.P;

/** http server 初始化启动类 */
public class HttpServerInit {
  public static HttpConfig httpConfig;
  public static HttpRequestHandler requestHandler;
  public static HttpServerStarter httpServerStarter;

  public static void init() throws Exception {

    int port = P.getInt("http.server.port"); // 启动端口
    httpConfig = new HttpConfig(port, null, null, null);
    httpConfig.setUseSession(false);
    httpConfig.setCheckHost(false);

    requestHandler =
        new DefaultHttpRequestHandler(httpConfig, IoTGatewayApplication.class); // 第二个参数也可以是数组

    httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
    httpServerStarter.start(); // 启动http服务器
  }
}
