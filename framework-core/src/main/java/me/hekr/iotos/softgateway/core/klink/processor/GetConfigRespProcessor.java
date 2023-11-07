package me.hekr.iotos.softgateway.core.klink.processor;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.DevSend;
import me.hekr.iotos.softgateway.core.klink.GetConfigResp;
import me.hekr.iotos.softgateway.core.klink.GetTopoResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.klink.ModelData;
import me.hekr.iotos.softgateway.core.network.mqtt.CoreMqttConnectedListenerImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设备登录流程，包含所有设备类型
 *
 * @author du
 * @see GetTopoRespProcessor#handle(GetTopoResp)
 * @see CoreMqttConnectedListenerImpl#onConnected()
 */
@Component
@Slf4j
public class GetConfigRespProcessor implements Processor<GetConfigResp> {
  @Autowired private KlinkService klinkService;
  @Autowired private IotOsConfig iotOsConfig;

  @Override
  public void handle(GetConfigResp klink) {
    handleConfig(klink);
  }

  private void handleConfig(GetConfigResp klink) {
    if (StringUtils.isBlank(klink.getUrl())) {
      log.warn("远程配置url为空，code:{}", klink.getCode());
    } else {
      log.info("远程配置url: {}", klink.getUrl());

      String content = HttpUtil.get(klink.getUrl(), 3000);
      log.info("config: -------------------\n{}\n-------------------\n", content);
      try {
        DeviceRemoteConfig.parseMultiLinesAndUpdateAll(content);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        // 解析出错，上报给网关
        klinkService.sendKlink(
            new DevSend(ModelData.cmd("reportError").param("error", e.getMessage())));
      }
    }

    addGateway();

    // 获取拓扑关系后，再进行比对然后注册设备
    log.info("发送 getTopo，获取拓扑关系");
    klinkService.getTopo();
  }

  /** 添加网关自身 */
  private void addGateway() {
    DeviceRemoteConfig gatewayConfig = new DeviceRemoteConfig();
    gatewayConfig = new DeviceRemoteConfig();
    gatewayConfig.setPk(iotOsConfig.getGatewayConfig().getPk());
    gatewayConfig.setDevId(iotOsConfig.getGatewayConfig().getDevId());
    gatewayConfig.setGateway(true);
    gatewayConfig.setOnline();
    DeviceRemoteConfig.updateByPkAndDevId(gatewayConfig);
  }

  @Override
  public Action getAction() {
    return Action.GET_CONFIG_RESP;
  }
}
