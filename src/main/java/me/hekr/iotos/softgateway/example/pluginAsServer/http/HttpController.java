package me.hekr.iotos.softgateway.example.pluginAsServer.http;

import lombok.extern.slf4j.Slf4j;
import me.hekr.iotos.softgateway.core.common.klink.KlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author iotos */
@Slf4j
@RestController
@RequestMapping(value = "/api")
public class HttpController {
  @Autowired KlinkService klinkService;

  @GetMapping("/demo")
  public String demo(@RequestParam String devId, @RequestParam String pk) {
    return "pk:" + pk + ",devId:" + devId;
  }
}
