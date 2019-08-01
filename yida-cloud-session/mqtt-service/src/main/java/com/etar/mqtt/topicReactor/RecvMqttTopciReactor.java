package com.etar.mqtt.topicReactor;

public interface RecvMqttTopciReactor {

    /**
     *  设备绑定
     */
    void onDevBind();

    /**
     *  设备解绑
     */
    void onDevUnBind();

    /**
     *  滤芯绑定
     */
    void onFilterBind();

    /**
     *  滤芯解绑
     */
    void onFilterUnBind();

    /**
     *   设备开机
     */
    void onDevStartup();


    /**
     *  设备广告应答
     */
    void onReportAdvReponse();


    /**
     *  设备上线
     */
    void onDevOnline();


    /**
     *  设备下线
     */
    void onDevOffline();
}
