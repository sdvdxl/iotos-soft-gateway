package com.example.iotcloudgateway.server.http;

import com.example.iotcloudgateway.mqtt.MqttServer;
import com.example.iotcloudgateway.server.tcp.TcpPacket;
import iot.cloud.os.common.utils.JsonUtil;
import iot.cloud.os.core.api.dto.klink.DevLogin;
import iot.cloud.os.core.api.dto.klink.KlinkDev;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
//    HttpResponse ret = Resps.html(request, "自定义的404");
    HttpResponse ret = Resps.bytes(request, TcpPacket.HTTP_RESP, "ok");
    return ret;
  }
}
