package me.hekr.iotos.softgateway.core.klink.processor;

import me.hekr.iotos.softgateway.core.annotation.CloudSendCommand;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig;
import me.hekr.iotos.softgateway.core.klink.CloudSend;
import me.hekr.iotos.softgateway.core.klink.ModelData;
import me.hekr.iotos.softgateway.core.subsystem.SubsystemCommandService;
import org.junit.Assert;
import org.junit.Test;

public class CloudSendProcessorTest {
  @Test
  public void testCloudSendCommandMatch() throws Exception {
    // 测试匹配全部命令
    CloudSend cloudSend = new CloudSend();
    cloudSend.setData(ModelData.cmd("cmd"));
    Assert.assertTrue(
        CloudSendProcessor.isCommandMatch(
            cloudSend, false, null, new CloudSendCommandServiceImpl()));

    Assert.assertTrue(
        CloudSendProcessor.isCommandMatch(
            cloudSend, false, null, new CloudSendCommandServiceImpl2()));

    Assert.assertFalse(
        CloudSendProcessor.isCommandMatch(
            cloudSend, false, null, new CloudSendCommandServiceImpl3()));
  }

  @CloudSendCommand
  public static class CloudSendCommandServiceImpl implements SubsystemCommandService {

    @Override
    public void handle(DeviceRemoteConfig deviceRemoteConfig, ModelData data) {}
  }

  @CloudSendCommand(cmd = "cmd")
  public static class CloudSendCommandServiceImpl2 implements SubsystemCommandService {

    @Override
    public void handle(DeviceRemoteConfig deviceRemoteConfig, ModelData data) {}
  }

  @CloudSendCommand(cmd = "cmd", type = "type")
  public static class CloudSendCommandServiceImpl3 implements SubsystemCommandService {

    @Override
    public void handle(DeviceRemoteConfig deviceRemoteConfig, ModelData data) {}
  }
}
