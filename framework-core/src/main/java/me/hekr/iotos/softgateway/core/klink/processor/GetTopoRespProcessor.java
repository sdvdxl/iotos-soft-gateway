package me.hekr.iotos.softgateway.core.klink.processor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.Dev;
import me.hekr.iotos.softgateway.core.klink.GetTopoResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.listener.DeviceRemoteConfigListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author du
 *     <p>设备登录流程，包含所有设备类型
 */
@Component
@Slf4j
public class GetTopoRespProcessor implements Processor<GetTopoResp> {
  private final AtomicBoolean firstBefore = new AtomicBoolean(false);
  private final AtomicBoolean firstAfter = new AtomicBoolean(false);

  @Autowired private KlinkService klinkService;

  @Autowired(required = false)
  private List<DeviceRemoteConfigListener> deviceRemoteConfigListeners;

  @Override
  public void handle(GetTopoResp klink) {
    handleTopo(klink);
  }

  private void handleTopo(GetTopoResp klink) {
    if (deviceRemoteConfigListeners != null) {
      for (DeviceRemoteConfigListener listener : deviceRemoteConfigListeners) {
        if (firstBefore.compareAndSet(false, true)) {
          try {
            listener.firstBefore();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          firstBefore.set(true);
        }
        try {
          listener.before();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    List<Dev> topoDevices = klink.getSubs();
    Set<DeviceRemoteConfig> all = DeviceRemoteConfig.getAll();
    // 查找拓扑中存在，但是配置中不存在的设备，删除拓扑关系
    Set<Dev> mappedDevices =
        all.stream()
            .map(d -> new Dev(d.getPk(), d.getDevId(), d.getDevName()))
            .collect(Collectors.toSet());
    Collection<Dev> devices = CollectionUtil.subtract(topoDevices, mappedDevices);
    log.info("需要删除拓扑的设备：{}", devices);
    for (Dev d : devices) {
      klinkService.delDev(d.getPk(), d.getDevId());
    }

    // 查找配置存在，但是拓扑不存在的设备，进行注册
    devices = CollectionUtil.subtract(mappedDevices, topoDevices);
    log.info("需要添加拓扑的设备：{}", devices);

    // 1. 先注册
    for (Dev d : devices) {
      klinkService.register(d.getPk(), d.getDevId(), d.getName());
    }

    ThreadUtil.sleep(1000);

    // 2. 添加拓扑
    for (Dev d : devices) {
      klinkService.addTopo(d.getPk(), d.getDevId());
    }

    log.info("设备同步完成");
    if (deviceRemoteConfigListeners != null) {

      for (DeviceRemoteConfigListener listener : deviceRemoteConfigListeners) {
        if (firstAfter.compareAndSet(false, true)) {
          try {
            listener.firstAfter();
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          firstAfter.set(true);
        }
        try {
          listener.after();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  @Override
  public Action getAction() {
    return Action.GET_TOPO_RESP;
  }
}
