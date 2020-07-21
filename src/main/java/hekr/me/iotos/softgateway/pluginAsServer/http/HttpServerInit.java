package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.IoTGatewayApplication;
import hekr.me.iotos.softgateway.common.config.ProxyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.utils.jfinal.P;

/** http server 初始化启动类 */
@Service
public class HttpServerInit {
  //  @Autowired ProxyConfig proxyConfig;

  @Value("${http.server.port}")
  private int HTTP_PORT;

  public HttpConfig httpConfig;
  public HttpRequestHandler requestHandler;
  public HttpServerStarter httpServerStarter;

  public HttpServerInit() throws Exception {

    int port = HTTP_PORT; // 启动端口
    httpConfig = new HttpConfig(port, null, null, null);
    httpConfig.setUseSession(false);
    httpConfig.setCheckHost(false);

    requestHandler =
        new DefaultHttpRequestHandler(httpConfig, IoTGatewayApplication.class); // 第二个参数也可以是数组

    httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
    httpServerStarter.start(); // 启动http服务器
  }
}
