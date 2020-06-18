package com.example.iotcloudgateway.SubMqtt;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *  connect方法用来将数据平台软网关连接至IoTOS
 */
public class MqttConnect {

    /**
     * 构造函数
     * @throws MqttException
     */
    public MqttConnect() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(MqttServer.HOST, clientid, new MemoryPersistence());
        connect();
    }

    public MqttClient client;
    public MqttTopic topic11;
    public MqttMessage message;

    //定义一个主题
    String UP_TOPIC = "up/dev/" + MqttServer.DEV_PK + "/" + MqttServer.DEV_ID;
    String DOWN_TOPIC = "down" +"/dev/" + MqttServer.DEV_PK + "/" + MqttServer.DEV_ID;
    // 定义MQTT的ID，可以在MQTT服务配置中指定// 网关的clientid
    String clientid = "dev:" + MqttServer.DEV_PK + ":" + MqttServer.DEV_ID;

    public void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(MqttServer.userName);
        options.setPassword(MqttServer.passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
//            client.subscribe();
            topic11 = client.getTopic(UP_TOPIC);
            //订阅
            client.subscribe(DOWN_TOPIC,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }
}

