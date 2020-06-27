package com.example.iotcloudgateway.server.http;

import com.example.iotcloudgateway.server.tcp.TcpPacket;
import lombok.extern.slf4j.Slf4j;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

@Slf4j
@RequestPath(value = "/gateway")
public class HttpController {

  public HttpController() {}

  @RequestPath(value = "/push")
  public HttpResponse page404(HttpRequest request) throws Exception {
    HttpResponse ret = Resps.bytes(request, TcpPacket.HTTP_RESP, "ok");
    return ret;
  }
}
