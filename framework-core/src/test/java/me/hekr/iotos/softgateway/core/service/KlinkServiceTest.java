package me.hekr.iotos.softgateway.core.service;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import me.hekr.iotos.softgateway.core.config.IotOsConfig;
import me.hekr.iotos.softgateway.core.dto.CacheDeviceKey;
import me.hekr.iotos.softgateway.core.klink.DevSend;
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
  @MockBean IotOsConfig iotOsConfig;
  @MockBean private MqttService mockMqttService;

  private KlinkService klinkService;

  @Before
  public void setUp() {
    iotOsConfig.setCacheParamsSize(1000);
    iotOsConfig.setCacheExpireSeconds(3600);
    klinkService = new KlinkService(iotOsConfig, mockMqttService);
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

  @Test
  public void testDevSendWithCache_CacheMiss() {
    String pk = "pk";
    String devId = "devId";
    ModelData data = new ModelData();
    Map<String, Object> params = new HashMap<>();
    params.put("param1", 1);
    data.setParams(params);
    data.setCmd("cmd");

    String cacheValue = null;
    CacheDeviceKey cacheDeviceKey = CacheDeviceKey.of(pk, devId, "param1");
    when(klinkService.getCACHE_PARAM_VALUE().getIfPresent(cacheDeviceKey)).thenReturn(cacheValue);

    klinkService.devSendWithCache(pk, devId, data);

    verify(klinkService.getCACHE_PARAM_VALUE(), times(1)).getIfPresent(cacheDeviceKey);
    verify(klinkService.getCACHE_PARAM_VALUE(), times(1)).put(cacheDeviceKey, "1");
    verify(mockMqttService, times(1)).publish(any(DevSend.class));
  }
}
