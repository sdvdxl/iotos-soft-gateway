package me.hekr.iotos.softgateway.network.http;

import java.util.List;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import org.junit.Test;

public class HttpClientTest {
  @Test
  public void testRequestPageable() {
    HttpClient client = HttpClient.newInstance("http://localhost:8080/");
    HttpRequestPageable<DeviceResponse> request =
        new HttpRequestPageable<DeviceResponse>() {
          @Override
          public HttpRequest buildPageRequest(int curPage, int pageSize) {
            return HttpRequest.builder()
                .path("")
                .addParam("curPage", curPage)
                .addParam("pageSize", pageSize)
                .build();
          }

          @Override
          public boolean hasMore(DeviceResponse resp) {
            return resp.hasMore();
          }
        };
    ResponseParser parser =
        response -> {
          DeviceResponse fromBytes;
          fromBytes = JsonUtil.fromBytes(response.bytes, DeviceResponse.class);
          return HttpPageResponse.wrap(fromBytes, fromBytes.devices);
        };
    List<Device> list = client.exec(request, parser, 0, 1);
    System.out.println(list);
  }
}
