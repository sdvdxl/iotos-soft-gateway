package com.example.iotcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.tio.core.starter.annotation.EnableTioServerServer;

import java.net.SocketException;

@SpringBootApplication
@EnableTioServerServer
@EnableFeignClients
public class IoTCloudGatewayApplication {
    public static void main(String[] args) throws SocketException {
        SpringApplication.run(IoTCloudGatewayApplication.class, args);
    }
}