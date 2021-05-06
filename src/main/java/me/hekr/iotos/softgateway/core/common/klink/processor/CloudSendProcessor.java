package me.hekr.iotos.softgateway.core.common.klink.processor;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.config.DeviceMapper;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.CloudSend;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import me.hekr.iotos.softgateway.core.utils.JsonUtil;
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
    // * <p>实现类要加 @Service("XXSubsystemCommandService")，其中 xx 为 iotos 命令
    SubsystemCommandService subsystemCommandService =
        context.getBean(
            klink.getData().getCmd() + "SubsystemCommandService", SubsystemCommandService.class);
    String pk = klink.getPk();
    String devId = klink.getDevId();
    Optional<DeviceMapper> dev = DeviceMapper.getByPkAndDevId(pk, devId);
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
