package hekr.me.iotos.softgateway;

import hekr.me.iotos.softgateway.northProxy.ProxyCallbackService;
import hekr.me.iotos.softgateway.northProxy.ProxyService;
import hekr.me.iotos.softgateway.northProxy.processor.CloudSendProcessor;
import hekr.me.iotos.softgateway.pluginAsClient.http.HttpClient;
import hekr.me.iotos.softgateway.pluginAsServer.http.HttpServerInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tio.utils.jfinal.P;

@SpringBootApplication
public class IoTGatewayApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(IoTGatewayApplication.class, args);

    // 获取配置文件中的相关参数
    P.use("config.properties");

    // 软网关初始化，完成软网关参数读取、登陆操作
    ProxyService.init();
    // 软网关对云端下发指令或回复指令进行相应处理的processor注册
    ProxyCallbackService.processorManager.register(new CloudSendProcessor());

    // 若要启用http则将下行注释打开
    HttpServerInit.init();

    //     使用http client示例
    Thread.sleep(5000);
    HttpClient.example();

    // 若要启用TCP client则将下行注释打开
    //    TcpClientStarter.start();
    // 若要启用TCP server则将下行注释打开
    //    TcpServerStarter.start();
    // 若要启用UDP client则将下行注释打开
    //           UdpClientStarter.start();
    // 若要启用UDP server则将下行注释打开
    //       UdpService.init();
  }

  public IoTGatewayApplication() {}
}
