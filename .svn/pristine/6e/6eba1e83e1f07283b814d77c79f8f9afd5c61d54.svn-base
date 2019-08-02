package com.etar.mqtt;


import entity.advstatic.AdvStatic;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MqttPushClientController控制层
 *
 * @author gmq
 * @version 1.0
 * @date 2019/3/18 13:41
 */
@RestController
public class MqttController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttController.class);
    private final MqttPushClient mqttPushClient;
    @Resource
    private com.etar.dev.IDevMngService IDevMngService;

    @Autowired
    public MqttController(MqttPushClient mqttPushClient) {
        this.mqttPushClient = mqttPushClient;
    }

//    @GetMapping("yida/mqtt/send")
//    public Result sendAd() {
//        DataResult result = new DataResult();
//        Advertising ad = new Advertising();
//        ad.setSolgan(AdWord.getSolgan());
//        ad.setTitle(AdWord.getTitle());
//        ad.setUpdateTime(new Date());
//        result.ok();
//        int id = new Random().nextInt(50000);
//        ad.setId(id);
//        String advMsg = MqttUtil.getAdvMsg(ad, ConstantUtil.MQTT_ADV_PREFIX_BROADCAST);
//        try {
//            LOGGER.info("广告id:{};广告下发的广告详情：{}", id, advMsg);
//            mqttPushClient.publish(1, "broadcast", advMsg);
//            result.setDatas(advMsg);
//            return result;
//        } catch (MqttException e) {
//            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
//                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
//                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
//                mqttPushClient.reconnect();
//                LOGGER.info("下发时间时客户机未连接");
//            } else {
//                LOGGER.info("Mqtt异常");
//            }
//            result.error(1, "fail");
//        }
//        return result;
//    }

    /**
     * 发布，默认qos为0，非持久化
     *
     * @param topic       订阅主题
     * @param pushMessage 发送的消息
     */
    @GetMapping("/publish")
    public int publish(@RequestParam(value = "qos") int qos, @RequestParam(value = "topic") String topic, @RequestParam(value = "pushMessage") String pushMessage) {
        try {

            return mqttPushClient.publish(qos, true, topic, pushMessage);
        } catch (MqttException e) {
            LOGGER.info(e.getMessage());
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                mqttPushClient.reconnect();
            } else {
                LOGGER.info("Mqtt异常");
            }
        }
        return 0xff;
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   订阅方式
     */
    @GetMapping("/subscribe")
    public void subscribe(@RequestParam(value = "topic") String topic, @RequestParam(value = "qos", required = false) int qos) {
        try {
            mqttPushClient.subscribe(topic, qos);
        } catch (MqttException e) {
            LOGGER.info(e.getMessage());
            if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING || e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_NOT_CONNECTED || e.getReasonCode() == MqttException.REASON_CODE_CONNECTION_LOST
                    || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECT_PROHIBITED || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_ALREADY_DISCONNECTED) {
                mqttPushClient.reconnect();
            } else {
                LOGGER.info("Mqtt异常");
            }
        }
    }

    /**
     * 重连
     */
    @GetMapping("/reconnect")
    public void reconnect() {
        mqttPushClient.reconnect();
    }

    /**
     * 保存广告应答对象<br/>
     *
     * @param advstatic
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public void save(AdvStatic advstatic) {
        IDevMngService.save(advstatic);
    }

}
