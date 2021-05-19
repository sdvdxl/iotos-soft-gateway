package me.hekr.iotos.softgateway.sample;

import java.util.List;
import lombok.Data;
import me.hekr.iotos.softgateway.network.http.PageableResponse;

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
