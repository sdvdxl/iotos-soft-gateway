package me.hekr.iotos.softgateway.core.config;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import me.hekr.iotos.softgateway.core.config.DeviceMapper.Props;
import org.junit.Test;

public class DeviceMapperTest {
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
    assertEquals(3, DeviceMapper.parseMultiLines(content).size());
  }

  @Test
  public void testUpdateAll() {
    DeviceMapper.updateAll(
        Collections.singletonList(
            DeviceMapper.parse("{\"pk\":\"pk_test\",\"devId\":\"1000004\",\"devName\":\"人脸识别\"}")));
    assertEquals(1, DeviceMapper.size());
    assertEquals("pk_test", DeviceMapper.getByPkAndDevId("pk_test", "1000004").get().getPk());
  }

  @Test
  public void testGetBySubSystemProperties() {
    DeviceMapper.parseMultiLinesAndUpdateAll(content);
    assertEquals(
        "devId003",
        DeviceMapper.getBySubSystemProperties(Props.p("devName", "devName003").get())
            .get()
            .getDevId());
  }
}
