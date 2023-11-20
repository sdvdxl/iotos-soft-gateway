package me.hekr.iotos.softgateway.core.service;

import java.util.HashMap;
import java.util.Map;
import me.hekr.iotos.softgateway.core.config.IotOsAutoConfiguration;
import me.hekr.iotos.softgateway.core.klink.KlinkService;
import me.hekr.iotos.softgateway.core.klink.ModelData;
import me.hekr.iotos.softgateway.core.network.mqtt.MqttService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KlinkService.class)
public class KlinkServiceTest {
  @MockBean
  IotOsAutoConfiguration iotOsAutoConfiguration;
  @MockBean private MqttService mockMqttService;

  private KlinkService klinkService;

  @Before
  public void setUp() {
    iotOsAutoConfiguration.setCacheParamsSize(1000);
    iotOsAutoConfiguration.setCacheExpireSeconds(3600);
    klinkService = new KlinkService(iotOsAutoConfiguration, mockMqttService);
  }

  @Test
  public void testDevSendWithCache_CacheHit() {
    String pk = "pk";
    String devId = "devId";
    ModelData data = new ModelData();
    Map<String, Object> params = new HashMap<>();
    params.put("param1", 1);
    data.setParams(params);
    data.setCmd("cmd");

    klinkService.devSendWithCache(pk, devId, data);
    klinkService.devSendWithCache(pk, devId, data);
  }
}
