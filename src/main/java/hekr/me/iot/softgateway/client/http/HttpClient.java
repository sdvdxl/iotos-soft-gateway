package hekr.me.iot.softgateway.client.http;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.tio.utils.jfinal.P;

/** */
@Slf4j
public class HttpClient {
  private String baseUrl = "http://" + P.get("http.client.connect.host");

  @SneakyThrows
  public void getAndSend() {
    // 此处填写访问的路径
    String url = baseUrl + "/gateway/test";
    HttpUtils httpUtils = new HttpUtils();
    byte[] response = httpUtils.get(url, null, null);
    System.out.println(new String(response));
  }
}
