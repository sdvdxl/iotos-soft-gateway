package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.dto.EnergyMeterResp;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.util.Resps;

/** */
@Slf4j
@RestController
@RequestMapping(value = "/api/bi")
public class HttpController {

//    @SneakyThrows
//    @PostMapping("/GetEnergyMeterData")
//    public HttpResponse postChargeRecord(HttpRequest request) {
//        // 将数据解码后
//        try {
//            EnergyMeterResp energyMeterResp = JsonUtil.fromBytes(request.getBody(), EnergyMeterResp.class);
//            if (checkChargeReq(chargeReq)) {
//                return Resps.json(request, getLackResp());
//            }
//            return Resps.json(request, getSuccessResp());
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//            return Resps.resp500(request, e);
//        }
//    }

}
