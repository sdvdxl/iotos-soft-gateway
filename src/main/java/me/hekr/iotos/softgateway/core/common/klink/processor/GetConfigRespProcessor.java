package me.hekr.iotos.softgateway.core.common.klink.processor;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.config.DeviceMapper;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.GetConfigResp;
import me.hekr.iotos.softgateway.core.common.klink.GetTopoResp;
import me.hekr.iotos.softgateway.core.common.klink.KlinkService;
import me.hekr.iotos.softgateway.core.network.mqtt.listener.CoreMqttConnectedListenerImpl;
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

  @Override
  public void handle(GetConfigResp klink) {
    handleConfig(klink);
  }

  private void handleConfig(GetConfigResp klink) {
    if (StringUtils.isBlank(klink.getUrl())) {
      log.warn("远程配置url为空，code:{}", klink.getCode());
      return;
    }
    String content = HttpUtil.get(klink.getUrl());
    log.info("config: {}", content);
    DeviceMapper.parseMultiLinesAndUpdateAll(content);

    // 获取拓扑关系后，再进行比对然后注册设备
    log.info("发送 getTopo，获取拓扑关系");
    klinkService.getTopo();
  }

  @Override
  public Action getAction() {
    return Action.GET_CONFIG_RESP;
  }
}
