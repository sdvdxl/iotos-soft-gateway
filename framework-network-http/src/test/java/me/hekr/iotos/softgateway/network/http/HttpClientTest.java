package me.hekr.iotos.softgateway.network.http;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HttpClientTest {

  /** 测试分页 */
  @Test
  public void testRequestPageable() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/");
    HttpRequestPageable<DeviceResponse> request =
        new HttpRequestPageable<DeviceResponse>(0, 10) {
          @Override
          public HttpRequest buildRequest(int curPage, int pageSize) {
            return HttpRequest.builder()
                .addParam("curPage", curPage)
                .addParam("pageSize", pageSize)
                .build();
          }

          @Override
          public boolean hasMore(DeviceResponse resp) {
            return resp.hasMore();
          }
        };
    PageableResponseParser<DeviceResponse, Device> parser =
        r -> HttpPageResponse.wrap(JsonUtil.fromBytes(r.bytes, DeviceResponse.class));
    List<Device> list = client.exec(request, parser, 0, 1);
    System.out.println(list);
  }

  /** 测试分页 ，紧紧返回列表的情况 */
  @Test
  public void testRequestPageableItems() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/");
    HttpRequestPageable<DeviceResponse> request =
        new HttpRequestPageable<DeviceResponse>(0, 10) {
          @Override
          public HttpRequest buildRequest(int curPage, int pageSize) {
            return HttpRequest.builder()
                .path("items")
                .addParam("curPage", curPage)
                .addParam("pageSize", pageSize)
                .build();
          }

          @Override
          public boolean hasMore(DeviceResponse resp) {
            return resp.hasMore();
          }
        };
    PageableResponseParser<DeviceResponse, Device> parser =
        r -> {
          List<Device> devices =
              JsonUtil.fromBytes(r.getBytes(), new TypeReference<List<Device>>() {});
          return new HttpPageResponse<>(new DeviceResponse(devices));
        };
    List<Device> list = client.exec(request, parser, 0, 1);
    System.out.println(list);
  }

  @Test(expected = HttpException.class)
  public void testHttpException() {
    HttpClient client = HttpClient.newInstance("http://localhost:1234/");
    client.exec(HttpRequest.builder().build(), DeviceResponse.class);
  }

  @Test
  public void testHttpExceptionLog() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/", Level.BODY);
    System.out.println(client.exec(HttpRequest.builder().build(), DeviceResponse.class));
  }
}
