package me.hekr.iotos.softgateway.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import me.hekr.iotos.softgateway.core.config.DeviceRemoteConfig.Props;
import org.junit.Test;

public class DeviceRemoteConfigTest {
  private static final String pk = "pk111";
  private static final String devId1 = "devId001";
  private static final String devId2 = "devId002";
  private static final String devId3 = "devId003";
  private static final String content;

  static {
    content =
        "{\"pk\":\""
            + pk
            + "\",\"devId\":\""
            + devId1
            + "\",\"devName\":\"devName001\"}\n"
            + "{\"pk\":\""
            + pk
            + "\",\"devId\":\""
            + devId2
            + "\",\"devName\":\"devName002\"}\n"
            + "{\"pk\":\""
            + pk
            + "\",\"devId\":\""
            + devId3
            + "\",\"devName\":\"devName003\"}\n";
    System.out.println(content);
  }

  @Test
  public void testParse() {
    assertEquals(3, DeviceRemoteConfig.parseMultiLines(content).size());
  }

  @Test
  public void testUpdateAll() {
    DeviceRemoteConfig.clear();
    DeviceRemoteConfig.updateAll(
        Collections.singletonList(
            DeviceRemoteConfig.parse(
                "{\"pk\":\"pk_test\",\"devId\":\"1000004\",\"devName\":\"人脸识别\"}")));
    assertEquals(1, DeviceRemoteConfig.size());
    assertEquals("pk_test", DeviceRemoteConfig.getByPkAndDevId("pk_test", "1000004").get().getPk());
  }

  @Test
  public void testGetBySubSystemProperties() {
    DeviceRemoteConfig.parseMultiLinesAndUpdateAll(content);
    assertEquals(
        "devId003",
        DeviceRemoteConfig.getBySubSystemProperties(Props.p("devName", "devName003").get())
            .get()
            .getDevId());
  }

  @Test
  public void testUpdate() {
    DeviceRemoteConfig d = new DeviceRemoteConfig();
    d.setPk("pk");
    d.setDevId("devId");
    DeviceRemoteConfig.update(d);
    Map<String, Object> map = new HashMap<>();
    map.put("pk", "pk");
    map.put("devId", "devId");
    map.put("deviceType", "A");
    DeviceRemoteConfig.update(new DeviceRemoteConfig(map));
    assertEquals("A", DeviceRemoteConfig.getByPkAndDevId("pk", "devId").get().getDeviceType());
  }

  @Test
  public void testEq(){
    Map<String,Object> params1 = new HashMap<>();
    params1.put("a",new Integer(1));
    Map<String,Object> params2 = new HashMap<>();
    params2.put("a",1);
    assertTrue(DeviceRemoteConfig.dataEq(params1,params2));
  }

}
