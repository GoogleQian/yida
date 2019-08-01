package com.zypcy.springcloud.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Gmq
 * @since 2019-05-09 14:04
 **/
//@Component
public class WebFilter extends ZuulFilter {
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		System.out.println("zuul过滤器...");
		//向header中添加鉴权令牌
		RequestContext requestContext = RequestContext.getCurrentContext();
		//获取header
		HttpServletRequest request = requestContext.getRequest();

		String scheme = request.getScheme();
		System.out.println("isHttps："+scheme);
		String authorization = request.getHeader("Accept");
		String encoding = request.getHeader("Accept-Encoding");

		if(authorization != null) {
			System.out.println("authorization: " + authorization);
			requestContext.addZuulRequestHeader("Authorization", authorization);
		}
		if(encoding != null) {
			System.out.println("Accept-Encoding: " + encoding);
			requestContext.addZuulRequestHeader("Accept-Encoding", encoding);
		}
		return null;
	}
}
