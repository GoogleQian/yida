package com.etar.mqtt.topicReactor;

import com.etar.dev.IDevMngService;

public class RecvMqttTopciReactorImpl implements RecvMqttTopciReactor {

    private String topic;

    private IDevMngService  iDevMngService;


    /**
     *   设置接收的主题
     * @param topic
     * @return
     */
    public RecvMqttTopciReactorImpl setTopic(String topic){
            this.topic = topic;
            return  this;
    }

    /**
     *  设置设备管理
     * @param iDevMngService
     * @return
     */
    public  RecvMqttTopciReactorImpl setDevMngSvr(IDevMngService iDevMngService){
            this.iDevMngService = iDevMngService;
            return this;
    }

    /**
     *  解析主题，并对相关主题进行应答
     */
    public  void start(){

            // 解析 topic, 并对topic 进行相应
    }

    @Override
    public void onDevBind() {

    }

    @Override
    public void onDevUnBind() {

    }

    @Override
    public void onFilterBind() {

    }

    @Override
    public void onFilterUnBind() {

    }

    @Override
    public void onDevStartup() {

    }

    @Override
    public void onReportAdvReponse() {

    }

    @Override
    public void onDevOnline() {

    }

    @Override
    public void onDevOffline() {

    }
}
