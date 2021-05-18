package me.hekr.iotos.softgateway.network.http;

import java.util.List;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import org.junit.Test;

public class HttpClientTest {
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
}
