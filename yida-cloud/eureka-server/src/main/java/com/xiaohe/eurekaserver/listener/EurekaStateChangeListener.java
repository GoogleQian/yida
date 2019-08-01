package com.xiaohe.eurekaserver.listener;

import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * eureka状态改变监听器
 *
 * @author  gmq
 * @date 2019/5/6
 */
@Component
public class EurekaStateChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(EurekaStateChangeListener.class);

	/**
	 * 区分生产环境
	 */
	@Value("${spring.profiles.active}")
	String active;

	/**
	 * 服务下线事件
	 *
	 * @param event
	 */
	@EventListener
	public void listenDown(EurekaInstanceCanceledEvent event) {
		if(active.equals("prod")){
			// 发送邮件
			String s = "服务ID：" + event.getServerId() + "\t" + "服务实例：" + event.getAppName() + "\t服务下线";
			SendMailUtil.send("772826099@qq.com", "宜达项目服务宕机警告", s, "smtp", "smtp.exmail.qq.com", "developer@he-live.com", "465", "developer@he-live.com", "XHDeveloper123");
			logger.info(MarkerFactory.getMarker("DOWN"),event.getServerId() + "\t" + event.getAppName() + "服务下线");
		}
	}

	/**
	 * 服务注册事件
	 *
	 * @param event
	 */
	@EventListener
	public void listenRegist(EurekaInstanceRegisteredEvent event) {
		InstanceInfo instanceInfo = event.getInstanceInfo();
		logger.info(MarkerFactory.getMarker("Register"), instanceInfo.getAppName() + "服务注册");
	}
}