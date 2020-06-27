package com.example.iotcloudgateway.client.udp;

import org.tio.core.udp.UdpClient;
import org.tio.core.udp.UdpClientConf;

public class UdpClientStarter {
    public static final byte[] GET_MSG = {1};
    private static UdpClientConf udpClientConf = new UdpClientConf("192.168.1.135", 3000, 5000);
    public static void start() {
        UdpClient udpClient = new UdpClient(udpClientConf);
        udpClient.start();
        udpClient.send(GET_MSG);
    }
}
