package me.hekr.iotos.softgateway.network.http;

import java.util.List;
import lombok.Data;

@Data
public class DeviceResponse implements PageableResponse<Device> {
  List<Device> devices;

  public DeviceResponse() {}

  public DeviceResponse(List<Device> devices) {
    this.devices = devices;
  }

  public boolean hasMore() {
    return devices != null && !devices.isEmpty();
  }

  @Override
  public List<Device> getItems() {
    return devices;
  }
}
