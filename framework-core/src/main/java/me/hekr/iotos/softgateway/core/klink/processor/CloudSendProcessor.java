package me.hekr.iotos.softgateway.core.klink.processor;

import static me.hekr.iotos.softgateway.core.constant.Constants.CMD_BEAN_SUFFIX;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.CloudSend;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import me.hekr.iotos.softgateway.common.utils.JsonUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/** @author iotos */
@Slf4j
@Component
public class CloudSendProcessor implements Processor<CloudSend> {
  List<SubsystemCommandService> subsystemCommandServices;
  @Autowired private ApplicationContext context;

  @Override
  public void handle(CloudSend klink) {
    // * <p>实现类要加 @Service(xx+CMD_BEAN_SUFFIX)，其中 xx 为 iotos 命令
    SubsystemCommandService subsystemCommandService;
    try {
      subsystemCommandService =
          context.getBean(
              klink.getData().getCmd() + CMD_BEAN_SUFFIX, SubsystemCommandService.class);
    } catch (BeansException e) {
      log.warn("没有配置命令处理器，命令：{}", klink.getData().getCmd());
      return;
    }

    String pk = klink.getPk();
    String devId = klink.getDevId();
    Optional<DeviceRemoteConfig> dev = DeviceRemoteConfig.getByPkAndDevId(pk, devId);
    if (!dev.isPresent()) {
      log.warn(
          "not found mapped device, pk: {}, devId:{}, will not handle this iot cloud send command: {}",
          pk,
          devId,
          JsonUtil.toJson(klink));
      return;
    }

    try {
      subsystemCommandService.handle(dev.get(), klink.getData());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
