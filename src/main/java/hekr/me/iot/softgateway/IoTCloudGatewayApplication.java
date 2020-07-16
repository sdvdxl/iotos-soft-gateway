package hekr.me.iot.softgateway;

import hekr.me.iot.softgateway.northProxy.MqttCallbackService;
import hekr.me.iot.softgateway.northProxy.MqttServer;
import hekr.me.iot.softgateway.northProxy.processor.CloudSendProcessor;
import hekr.me.iot.softgateway.pluginAsServer.http.HttpServerInit;
import org.tio.utils.jfinal.P;

public class IoTCloudGatewayApplication {

  public static void main(String[] args) throws Exception {
    // 获取配置文件中的相关参数
    P.use("config.properties");

    // 软网关初始化，完成软网关参数读取、登陆操作
    MqttServer.init();
    // 软网关对云端下发指令或回复指令进行相应处理的processor注册
    MqttCallbackService.processorManager.register(new CloudSendProcessor());

    // 若要启用http则将下行注释打开
        HttpServerInit.init();

    // 使用http client示例
    //    Thread.sleep(5000);
    //    HttpClient httpClient = new HttpClient();
    //    httpClient.getAndSend();

    // 若要启用TCP client则将下行注释打开
    //    TcpClientStarter.start();
    // 若要启用TCP server则将下行注释打开
    //    TcpServerStarter.start();
    // 若要启用UDP client则将下行注释打开
    //           UdpClientStarter.start();
    // 若要启用UDP server则将下行注释打开
    //       UdpService.init();
  }

  public IoTCloudGatewayApplication() {}
}
