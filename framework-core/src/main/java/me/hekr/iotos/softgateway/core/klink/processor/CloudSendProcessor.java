package me.hekr.iotos.softgateway.core.klink.processor;

import static me.hekr.iotos.softgateway.core.constant.Constants.CMD_BEAN_SUFFIX;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.annotation.CloudSendCommand;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
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
 */
@Slf4j
@Component
public class CloudSendProcessor implements Processor<CloudSend> {
  @Autowired(required = false)
  private List<SubsystemCommandService> subsystemCommandServices;

  @Autowired private ApplicationContext context;
  @Autowired private KlinkService klinkService;
  @Autowired private IotOsConfig iotOsConfig;

  @PostConstruct
  public void init() {
    if (subsystemCommandServices == null) {
      subsystemCommandServices = Collections.emptyList();
    }
  }

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
    DeviceRemoteConfig deviceRemoteConfig;
    if (iotOsConfig.getGatewayConfig().isGateway(pk, devId)) {
      deviceRemoteConfig = new DeviceRemoteConfig();
      deviceRemoteConfig.setPk(pk);
      deviceRemoteConfig.setDevId(devId);
      deviceRemoteConfig.setGateway(true);
      deviceRemoteConfig.setOnline();
    } else {
      Optional<DeviceRemoteConfig> dev = DeviceRemoteConfig.getByPkAndDevId(pk, devId);
      if (!dev.isPresent()) {
        throw new CloudSendException(pk, devId, klink.getData(), "没找到对应的子系统设备");
      }

      deviceRemoteConfig = dev.get();
    }

    handleCommandAnnotationListeners(klink, deviceRemoteConfig);

    // FIXME 下个版本删除
    handleBeanCommandListener(klink, pk, devId, deviceRemoteConfig);
  }

  /**
   * 处理bean name方式的命令；废弃
   *
   * @param klink
   * @param pk
   * @param devId
   * @param deviceRemoteConfig
   */
  @Deprecated
  private void handleBeanCommandListener(
      CloudSend klink, String pk, String devId, DeviceRemoteConfig deviceRemoteConfig) {
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
      log.info("pk:{}, devId:{}, data:{} 没有配置下发命令(cloudSend)处理器", pk, devId, klink.getData());
      return;
    }

    subsystemCommandService.handle(deviceRemoteConfig, klink.getData());
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
          boolean match = isCommandMatch(klink, deviceRemoteConfig, l);
          if (match) {
            try {
              l.handle(deviceRemoteConfig, klink.getData());
            } catch (Exception e) {
              log.error("调用方法失败， " + l.getClass().getName() + ", " + e.getMessage(), e);
            }
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
   * @param deviceRemoteConfig
   * @param l
   * @return
   */
  private boolean isCommandMatch(
      CloudSend klink, DeviceRemoteConfig deviceRemoteConfig, SubsystemCommandService l) {
    CloudSendCommand cloudSendCommand =
        AnnotationUtils.findAnnotation(l.getClass(), CloudSendCommand.class);
    if (cloudSendCommand == null) {
      return false;
    }

    // cmd, type, param,
    String[] cmds = cloudSendCommand.cmd();
    String[] types = cloudSendCommand.type();
    boolean deviceTypeMatch = cloudSendCommand.gateway() == deviceRemoteConfig.isGateway();
    // 设备类型不匹配
    if (!deviceTypeMatch) {
      return false;
    }

    boolean match = false;
    for (String cmd : cmds) {
      if (klink.getData().getCmd().equals(cmd)) {
        match = true;
        break;
      }
    }

    if (!match) {
      return false;
    }

    if (types.length > 0) {
      match = Arrays.asList(types).contains(deviceRemoteConfig.getDeviceType());
    }
    return match;
  }

  @Override
  public Action getAction() {
    return Action.CLOUD_SEND;
  }
}
