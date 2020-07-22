package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.codec.DataCodec;
import hekr.me.iotos.softgateway.common.codec.RawDataCodec;
import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.ChargeReq;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

/** */
@Slf4j
@Service
@RequestPath(value = "/gateway")
public class HttpController {

  public HttpController() {}

  @SneakyThrows
  @RequestPath(value = "/postInOutRecord")
  public HttpResponse postInOutRecord(HttpRequest request) {
    // 将数据解码后
    try {
      //      EntranceReq entranceReq =
      //          JsonUtil.fromBytes(AESUtils.decodeRequestData(request.getBody()),
      // EntranceReq.class);
      EntranceReq entranceReq = JsonUtil.fromBytes(request.getBody(), EntranceReq.class);
      if (checkEntranceReq(entranceReq)) {
        return Resps.json(request, getLackResp());
      }
      return Resps.json(request, getSuccessResp());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Resps.resp500(request, e);
    }
  }

  @SneakyThrows
  @RequestPath(value = "/postChargeRecord")
  public HttpResponse postChargeRecord(HttpRequest request) {
    // 将数据解码后
    try {
      //      EntranceReq entranceReq =
      //          JsonUtil.fromBytes(AESUtils.decodeRequestData(request.getBody()),
      // EntranceReq.class);
      ChargeReq chargeReq = JsonUtil.fromBytes(request.getBody(), ChargeReq.class);
      if (checkChargeReq(chargeReq)) {
        return Resps.json(request, getLackResp());
      }
      return Resps.json(request, getSuccessResp());
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Resps.resp500(request, e);
    }
  }

  private BaseResp getLackResp() {
    return new BaseResp(403, "缺少参数");
  }

  private BaseResp getSuccessResp() {
    return new BaseResp(0, "成功");
  }

  private boolean checkEntranceReq(EntranceReq entranceReq) {
    return checkNull(
        entranceReq.getCarCode(),
        entranceReq.getInTime(),
        entranceReq.getPassTime(),
        entranceReq.getParkID(),
        entranceReq.getInOrOut(),
        entranceReq.getGUID(),
        entranceReq.getChannelID());
  }

  private boolean checkChargeReq(ChargeReq chargeReq) {
    return checkNull(
        chargeReq.getRecordID(),
        chargeReq.getCarCode(),
        chargeReq.getInTime(),
        chargeReq.getPayTime(),
        chargeReq.getParkID(),
        chargeReq.getChargeMoney(),
        chargeReq.getPaidMoney(),
        chargeReq.getJMMoney(),
        chargeReq.getChargeType(),
        chargeReq.getChargeSource(),
        chargeReq.getAmountType());
  }

  private boolean checkNull(Object... objects) {
    for (Object object : objects) {
      if (object == null) {
        return true;
      }
    }
    return false;
  }
}
