package hekr.me.iotos.softgateway.pluginAsClient.http;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tio.utils.jfinal.P;

/**
 * 此类为http协议客户端代码示例
 *
 * 用户可以根据对接的http server接口进行相应的代码编写
 *
 * 如下example()方法
 * */
@Slf4j
public class HttpClient {
  private String baseUrl = "http://" + P.get("http.client.connect.host");

  @SneakyThrows
  public void example() {
    // 此处填写访问的路径
    String url = baseUrl + "/gateway/test";
    HttpUtils httpUtils = new HttpUtils();
    byte[] response = httpUtils.get(url, null, null);
    System.out.println(new String(response));
  }
}
