package hekr.me.iotcloudgateway.server.http;

import hekr.me.iotcloudgateway.IoTCloudGatewayApplication;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.utils.jfinal.P;

/**
 * @author jiatao
 * @date 2020/6/24
 */
public class HttpServerInit {
  public static HttpConfig httpConfig;
  public static HttpRequestHandler requestHandler;
  public static HttpServerStarter httpServerStarter;

  public static void init() throws Exception {

    int port = P.getInt("http.port"); // 启动端口
    String pageRoot = P.get("http.page"); // html/css/js等的根目录，支持classpath:，也支持绝对路径
    httpConfig = new HttpConfig(port, null, null, null);
    httpConfig.setPageRoot(pageRoot);
    httpConfig.setMaxLiveTimeOfStaticRes(P.getInt("http.maxLiveTimeOfStaticRes"));
    httpConfig.setPage404(P.get("http.404"));
    httpConfig.setPage500(P.get("http.500"));
    httpConfig.setUseSession(false);
    httpConfig.setCheckHost(false);

    requestHandler =
        new DefaultHttpRequestHandler(httpConfig, IoTCloudGatewayApplication.class); // 第二个参数也可以是数组

    httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
    httpServerStarter.start(); // 启动http服务器
  }
}