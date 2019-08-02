package com.etar.config;


import com.etar.dev.IDevMngService;
import com.etar.emqclient.service.EmqClientService;
import com.etar.firmware.service.FirmwareService;
import com.etar.firmware.service.FirmwareStaticService;
import com.etar.mqtt.service.IMessageArrivedService;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author wzq
 */
@Component
@EnableAutoConfiguration
public class ApplicationContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    public static FirmwareStaticService getFirmwareStaticService() {
        return ApplicationContextUtils.applicationContext.getBean(FirmwareStaticService.class);
    }

    public static FirmwareService getFirmwareService() {
        return ApplicationContextUtils.applicationContext.getBean(FirmwareService.class);
    }

    public static IDevMngService getIDevMngService() {
        return ApplicationContextUtils.applicationContext.getBean(IDevMngService.class);
    }

    public static EmqClientService getEmqClientService() {
        return ApplicationContextUtils.applicationContext.getBean(EmqClientService.class);
    }

    public static IMessageArrivedService getIMessageArrivedService() {
        return ApplicationContextUtils.applicationContext.getBean(IMessageArrivedService.class);
    }


}
