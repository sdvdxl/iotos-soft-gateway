package me.hekr.iotos.softgateway.network.mqtt.processor;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.common.config.DeviceMapper;
import me.hekr.iotos.softgateway.common.enums.Action;
import me.hekr.iotos.softgateway.common.klink.GetConfigResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author du
 *     <p>设备登录流程，包含所有设备类型
 */
@Component
@Slf4j
public class GetConfigRespProcessor implements Processor<GetConfigResp> {

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
    DeviceMapper.updateAll(DeviceMapper.parseMultiLines(content));

    // 拉取设备拓扑关系，等待返回更新 -> getTopoRespProcessor
    //    mqttService.getTopo();
  }

  @Override
  public Action getAction() {
    return Action.GET_CONFIG_RESP;
  }
}
