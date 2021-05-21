package me.hekr.iotos.softgateway.core.klink.processor;

import static me.hekr.iotos.softgateway.core.constant.Constants.CMD_BEAN_SUFFIX;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;
import me.hekr.iotos.softgateway.core.exception.CloudSendException;
import me.hekr.iotos.softgateway.core.klink.CloudSend;
import me.hekr.iotos.softgateway.core.klink.CloudSendResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 处理下发命令，会自动回复 cloudSendResp。
 *
 * <p>如果抛出CloudSendException异常，则错误码是400，其他异常500.
 *
 * <p>不抛出异常视为成功，错误码为0
 *
 * @author iotos
 */
@Slf4j
@Component
public class CloudSendProcessor implements Processor<CloudSend> {
  List<SubsystemCommandService> subsystemCommandServices;
  @Autowired private ApplicationContext context;
  @Autowired private KlinkService klinkService;

  @Override
  public void handle(CloudSend klink) {
    CloudSendResp resp = new CloudSendResp();
    resp.setPk(klink.getPk());
    resp.setDevId(klink.getDevId());
    resp.setMsgId(klink.getMsgId());

    try {
      doHandleCloudSend(klink);
      resp.setErrorCode(ErrorCode.SUCCESS);
    } catch (CloudSendException e) {
      resp.setCode(400);
      resp.setDesc(e.getMessage());
      log.warn(e.getMessage());
    } catch (Exception e) {
      resp.setCode(500);
      resp.setDesc(e.getMessage());
      log.error(e.getMessage(), e);
    }

    klinkService.sendKlink(resp);
  }

  private void doHandleCloudSend(CloudSend klink) {
    String pk = klink.getPk();
    String devId = klink.getDevId();
    Optional<DeviceRemoteConfig> dev = DeviceRemoteConfig.getByPkAndDevId(pk, devId);
    if (!dev.isPresent()) {
      throw new CloudSendException(pk, devId, klink.getData(), "没找到对应的子系统设备");
    }

    DeviceRemoteConfig deviceRemoteConfig = dev.get();
    String beanName = klink.getData().getCmd();
    if (deviceRemoteConfig.getDeviceType() != null) {
      beanName = beanName + "#" + deviceRemoteConfig.getDeviceType();
    }
    beanName += CMD_BEAN_SUFFIX;

    // * <p>实现类要加 @Service(xx+CMD_BEAN_SUFFIX)，其中 xx 为 iotos 命令
    SubsystemCommandService subsystemCommandService;
    try {
      subsystemCommandService = context.getBean(beanName, SubsystemCommandService.class);
    } catch (BeansException e) {
      throw new CloudSendException(pk, devId, klink.getData(), "没有配置命令处理器");
    }

    subsystemCommandService.handle(deviceRemoteConfig, klink.getData());
  }

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
