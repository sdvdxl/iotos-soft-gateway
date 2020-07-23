package hekr.me.iotos.softgateway.pluginAsServer.http;

import hekr.me.iotos.softgateway.common.dto.BaseResp;
import hekr.me.iotos.softgateway.common.dto.ChargeReq;
import hekr.me.iotos.softgateway.common.dto.EntranceReq;
import hekr.me.iotos.softgateway.common.enums.Action;
import hekr.me.iotos.softgateway.common.klink.DevSend;
import hekr.me.iotos.softgateway.common.klink.ModelData;
import hekr.me.iotos.softgateway.northProxy.ProxyService;
import hekr.me.iotos.softgateway.northProxy.device.Device;
import hekr.me.iotos.softgateway.northProxy.device.DeviceService;
import hekr.me.iotos.softgateway.northProxy.device.DeviceType;
import hekr.me.iotos.softgateway.utils.AESUtils;
import hekr.me.iotos.softgateway.utils.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.util.Resps;

/** */
@Slf4j
@RestController
@RequestMapping(value = "/gateway")
public class HttpController {

  @Autowired private DeviceService deviceService;

  @Autowired private ProxyService proxyService;

  public HttpController() {}

  @SneakyThrows
  @PostMapping("/postInOutRecord")
  public Object postInOutRecord(@RequestBody String body) {

    // 将数据解码后
    try {
      EntranceReq entranceReq =
          JsonUtil.fromBytes(
              AESUtils.decodeRequestData(body), EntranceReq.class);
      //      EntranceReq entranceReq = JsonUtil.fromBytes(request.getBody(), EntranceReq.class);
      //      if (checkEntranceReq(entranceReq)) {
      //        return Resps.json(request, getLackResp());
      //      }

      Device device = deviceService.getByIdAndType(entranceReq.getChannelID(), DeviceType.BARRIER);

      DevSend devSend = new DevSend();
      devSend.setDevId(device.getDevId());
      devSend.setPk(device.getPk());

      ModelData data = new ModelData();
      data.setCmd("reportInOrOut");
      Map<String, Object> resp = new HashMap<>();
      resp.put("GUID", entranceReq.getGuid());
      resp.put("carCode", entranceReq.getCarCode());
      resp.put("imagePath", entranceReq.getImagePath());
      resp.put("inOrOut", entranceReq.getInOrOut());
      resp.put("inTime", entranceReq.getInTime());
      resp.put("parkID", entranceReq.getParkID());
      resp.put("passTime", entranceReq.getPassTime());

      data.setParams(resp);
      devSend.setData(data);
      devSend.setAction(Action.DEV_SEND.getAction());
      proxyService.devSend(devSend);

      return getSuccessResp();
    } catch (Exception e) {
      log.warn(e.getMessage());
      throw e;
    }
  }

  @SneakyThrows
  @PostMapping("/postChargeRecord")
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
        entranceReq.getGuid(),
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
