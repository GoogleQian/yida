package com.etar.purifier.modules.cache.impl;

import com.etar.purifier.modules.cache.CaptchaInfo;
import com.etar.purifier.modules.cache.CaptchaService;
import org.apache.shiro.cache.Cache;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ConstantUtil;

@Service
public class CaptchaServiceImpl implements CaptchaService {
	@Autowired
	private RedisCacheManager redisCacheManager;

	@Override
	public void setCaptcha(CaptchaInfo captchaInfo) {

		System.out.println(redisCacheManager.getExpire());
		try {
			//设置验证码过期时间
			redisCacheManager.setExpire(ConstantUtil.CODE_EXPIRE);
			redisCacheManager.setKeyPrefix(ConstantUtil.LOGIN_VERCODE_PRE);
			Cache<Object, Object> captcha = redisCacheManager.getCache(ConstantUtil.CAPTCHA);
			captcha.put(captchaInfo.getId(), captchaInfo.getCaptcha());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getCaptcha(String key) {
		try {
			//设置验证码过期时间
			redisCacheManager.setExpire(ConstantUtil.CODE_EXPIRE);
			redisCacheManager.setKeyPrefix(ConstantUtil.LOGIN_VERCODE_PRE);
			Cache<Object, Object> captcha = redisCacheManager.getCache(ConstantUtil.CAPTCHA);
			return captcha.get(key) == null ? "" : captcha.get(key).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	@Override
	public boolean remove(Integer key) {
		boolean flag = false;
		try {
			//设置验证码过期时间
			redisCacheManager.setExpire(ConstantUtil.CODE_EXPIRE);
			redisCacheManager.setKeyPrefix(ConstantUtil.LOGIN_VERCODE_PRE);
			Cache<Object, Object> captcha = redisCacheManager.getCache(ConstantUtil.CAPTCHA);
			Object o = captcha.remove(key);
			if (o != null) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}
}
