package me.hekr.iotos.softgateway.subsystem.service;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.config.DeviceMapper;
import me.hekr.iotos.softgateway.core.klink.ModelData;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import org.springframework.stereotype.Service;

/**
 * demo 命令测试用例，如果需要处理，则需要在 iot 上物模型中定义一个 demo 的命令
 *
 * @see me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService#handle(DeviceMapper,
 *     ModelData)
 * @author iot
 */
@Service("demoSubSystemCommandService")
@Slf4j
public class DemoSubSystemCommandServiceImpl implements SubsystemCommandService {

  @Override
  public void handle(DeviceMapper device, ModelData data) {
    log.info("设备收到命令， 设备：{}，命令：{}", device, data);
  }
}
