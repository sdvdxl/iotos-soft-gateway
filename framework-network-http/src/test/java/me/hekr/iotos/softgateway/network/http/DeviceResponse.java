package me.hekr.iotos.softgateway.network.http;

import java.util.List;
import lombok.Data;

@Data
public class DeviceResponse {
  List<Device> devices;

  public boolean hasMore() {
    return devices != null && !devices.isEmpty();
  }
}
