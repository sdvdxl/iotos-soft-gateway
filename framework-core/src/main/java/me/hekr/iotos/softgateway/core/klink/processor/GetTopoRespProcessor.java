package me.hekr.iotos.softgateway.core.klink.processor;

import cn.hutool.core.collection.CollectionUtil;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.enums.Action;
import me.hekr.iotos.softgateway.core.klink.Dev;
import me.hekr.iotos.softgateway.core.klink.GetTopoResp;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author du
 *     <p>设备登录流程，包含所有设备类型
 */
@Component
@Slf4j
public class GetTopoRespProcessor implements Processor<GetTopoResp> {
  @Autowired private KlinkService klinkService;

  @Override
  public void handle(GetTopoResp klink) {
    handleTopo(klink);
  }

  private void handleTopo(GetTopoResp klink) {
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
    for (Dev d : devices) {
      klinkService.addDev(d.getPk(), d.getDevId(), d.getName());
    }

    log.info("设备同步完成");
  }

  @Override
  public Action getAction() {
    return Action.GET_TOPO_RESP;
  }
}
