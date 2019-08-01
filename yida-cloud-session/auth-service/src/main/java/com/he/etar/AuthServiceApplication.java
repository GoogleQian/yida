package com.he.etar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @program zuth-service
 * @description: 启动类
 * @author: Gmq
 * @date: 2019-05-23 10:41
 **/
@SpringBootApplication
@Configuration
@EnableCaching
@EnableFeignClients(basePackages = "com.he.etar")
@EntityScan(value = {"entity.*"})
public class AuthServiceApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = SpringApplication.run(AuthServiceApplication.class, args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		for (String activeProfile : activeProfiles) {
			System.out.println("activeProfile = " + activeProfile);
		}
	}

}
