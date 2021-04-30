package me.hekr.iotos.softgateway.core.pluginAsServer.http;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.enums.Action;
import me.hekr.iotos.softgateway.core.common.klink.DevSend;
import me.hekr.iotos.softgateway.core.common.klink.KlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** */
@Slf4j
@RestController
@RequestMapping(value = "/gateway")
public class HttpController {
  @Autowired KlinkService klinkService;

  @PostMapping("/devSend")
  public Object devSend(@RequestBody DevSend devSend) {
    devSend.setAction(Action.DEV_SEND.getAction());
    klinkService.devSend(devSend);
    return null;
  }

  @GetMapping("/register")
  public Object register(
      @RequestParam String devId, @RequestParam String pk, @RequestParam String devName) {
    klinkService.register(pk, devId, null, devName);
    klinkService.addDev(pk, devId, null);
    return null;
  }

  @GetMapping("/login")
  public Object login(@RequestParam String devId, @RequestParam String pk) {
    klinkService.devLogin(pk, devId);
    return null;
  }
}
