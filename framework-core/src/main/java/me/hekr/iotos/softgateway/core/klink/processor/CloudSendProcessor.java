package me.hekr.iotos.softgateway.core.klink.processor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.annotation.CloudSendCommand;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.IotOsAutoConfiguration;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.enums.ErrorCode;
import me.hekr.iotos.softgateway.core.exception.CloudSendException;
import me.hekr.iotos.softgateway.core.klink.CloudSend;
import me.hekr.iotos.softgateway.core.klink.CloudSendResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 处理下发命令，会自动回复 cloudSendResp。
 *
 * <p>如果抛出CloudSendException异常，则错误码是400，其他异常500.
 *
 * <p>不抛出异常视为成功，错误码为0
 *
 * @author iotos
 * @version $Id: $Id
 */
@Slf4j
@Component
public class CloudSendProcessor implements Processor<CloudSend> {
  @Autowired(required = false)
  private List<SubsystemCommandService> subsystemCommandServices;

  @Autowired private ApplicationContext context;
  @Autowired private KlinkService klinkService;
  @Autowired private IotOsAutoConfiguration iotOsAutoConfiguration;

  /**
   * <p>init.</p>
   */
  @PostConstruct
  public void init() {
    if (subsystemCommandServices == null) {
      subsystemCommandServices = Collections.emptyList();
    }
  }

  /** {@inheritDoc} */
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

    // 如果配置为自动回复，则发送 cloudSendResp
    if (iotOsAutoConfiguration.getMqttConfig().isAutoCloudSendResp()) {
      klinkService.sendKlink(resp);
    }
  }

  private void doHandleCloudSend(CloudSend klink) {
    String pk = klink.getPk();
    String devId = klink.getDevId();

    Optional<DeviceRemoteConfig> dev = DeviceRemoteConfig.getByPkAndDevId(pk, devId);
    if (!dev.isPresent()) {
      throw new CloudSendException(pk, devId, klink.getData(), "没找到对应的子系统设备");
    }

    handleCommandAnnotationListeners(klink, dev.get());
  }

  /**
   * 处理注解的监听器。
   *
   * <p>所有符合条件的处理器都会被调用
   *
   * @param klink
   * @param deviceRemoteConfig
   */
  private void handleCommandAnnotationListeners(
      CloudSend klink, DeviceRemoteConfig deviceRemoteConfig) {
    // 遍历所有的subsystemCommandServices，找到匹配的注解的bean
    subsystemCommandServices.forEach(
        l -> {
          boolean match =
              isCommandMatch(
                  klink, deviceRemoteConfig.isGateway(), deviceRemoteConfig.getDeviceType(), l);
          if (match) {
            l.handle(deviceRemoteConfig, klink.getData());
          } else {
            log.warn(
                "pk:{}, devId:{}, data:{} 没有配置下发命令(cloudSend)处理器",
                klink.getPk(),
                klink.getDevId(),
                klink.getData());
          }
        });
  }

  /**
   * 处理带注解的下发消息监听器
   *
   * @param klink
   * @param deviceIsGateway 当前访问的设备是否是网关
   * @param l
   * @return 是否匹配
   */
  static boolean isCommandMatch(
      CloudSend klink, boolean deviceIsGateway, String deviceType, SubsystemCommandService l) {
    CloudSendCommand cloudSendCommand =
        AnnotationUtils.findAnnotation(l.getClass(), CloudSendCommand.class);
    if (cloudSendCommand == null) {
      return false;
    }

    boolean match = cloudSendCommand.gateway() == deviceIsGateway;
    // 设备类型不匹配
    if (!match) {
      return false;
    }

    // cmd, type
    String[] cmds = cloudSendCommand.cmd();
    if (cmds.length > 0) {
      match = false;
      for (String cmd : cmds) {
        if (klink.getData().getCmd().equals(cmd)) {
          match = true;
          break;
        }
      }

      if (!match) {
        return false;
      }
    }

    String[] types = cloudSendCommand.type();
    if (types.length > 0) {
      match = Arrays.asList(types).contains(deviceType);
    }
    return match;
  }

  /** {@inheritDoc} */
  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
