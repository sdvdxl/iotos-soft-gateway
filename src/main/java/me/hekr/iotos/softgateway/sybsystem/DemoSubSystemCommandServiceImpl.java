package me.hekr.iotos.softgateway.sybsystem;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.config.DeviceMapper;
import me.hekr.iotos.softgateway.core.common.klink.ModelData;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import org.springframework.stereotype.Service;

/**
 * demo 命令测试用例，如果需要处理，则需要在 iot 上物模型中定义一个 demo 的命令
 *
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
