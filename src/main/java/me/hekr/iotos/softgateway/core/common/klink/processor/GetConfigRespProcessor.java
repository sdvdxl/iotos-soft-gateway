package me.hekr.iotos.softgateway.core.common.klink.processor;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.config.DeviceMapper;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.GetConfigResp;
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
    DeviceMapper.parseMultiLinesAndUpdateAll(content);
  }

  @Override
  public Action getAction() {
    return Action.GET_CONFIG_RESP;
  }
}
