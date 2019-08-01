package com.etar.purifier.config.shrio;


import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jack
 */
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("myAccessControlFilter", new MyAccessControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒

        filterChainDefinitionMap.put("/access/logout", "logout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/sellProgram/findAll", "anon");
        filterChainDefinitionMap.put("/access/login", "anon");
        filterChainDefinitionMap.put("/access/captcha", "anon");
        //小程序
        filterChainDefinitionMap.put("/yida/miniProgram/*", "anon");
        filterChainDefinitionMap.put("/yida/miniProgram/*/*", "anon");
        filterChainDefinitionMap.put("/yida/miniProgram/bind/*/*", "anon");
        filterChainDefinitionMap.put("/yida/miniProgram/**", "anon");
        //设备认证接口开放
        filterChainDefinitionMap.put("/yida/dev/auth", "anon");
        filterChainDefinitionMap.put("/yida/dev/aclAuth", "anon");
        //回调地址 设备上下线通知
        filterChainDefinitionMap.put("/emq/*", "anon");
        //图片上传
        filterChainDefinitionMap.put("/yida/file/image", "anon");
        filterChainDefinitionMap.put("/static/imgs/*", "anon");
        //切换主从数据源
        filterChainDefinitionMap.put("/switchDb/**", "anon");
        //目前先放开MQTT调用
        filterChainDefinitionMap.put("/yida/dev/updateBindStatus", "anon");
        filterChainDefinitionMap.put("/yida/dev/findByDevCode", "anon");
        filterChainDefinitionMap.put("/yida/dev/existsByDevCode", "anon");
        filterChainDefinitionMap.put("/yida/dev/hardwareUnbind", "anon");
        filterChainDefinitionMap.put("/yida/dev/updateOnline", "anon");
        filterChainDefinitionMap.put("/yida/dev/updateOnlineAndIpAndTime", "anon");
        filterChainDefinitionMap.put("/yida/dev/updateFilterLife", "anon");
        filterChainDefinitionMap.put("/yida/dev/devices", "anon");
        filterChainDefinitionMap.put("/yida/dev/existsByDevCode", "anon");
        filterChainDefinitionMap.put("/yida/dev/updateFilterLifeAndActive", "anon");
        filterChainDefinitionMap.put("/yida/filterInfos/updateFilterCode", "anon");
        filterChainDefinitionMap.put("/yida/ad/findById", "anon");
        filterChainDefinitionMap.put("/advstatic/save", "anon");

        filterChainDefinitionMap.put("/yida/*/test", "anon");

//        filterChainDefinitionMap.put("/**", "anon"); // 调试状态下，可开启本行，同时注释掉下面的行
        filterChainDefinitionMap.put("/**", "myAccessControlFilter");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/access/unauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyCredentialsMatcher getCredentialsMatcher() {
        return new MyCredentialsMatcher();
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(getCredentialsMatcher());
        return myShiroRealm;
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager() {
        MySessionMng mySessionManager = new MySessionMng();
        mySessionManager.setSessionIdCookieEnabled(false);
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO =new RedisSessionDAO();
        RedisManager redisManager = redisManager();
        //redis超时时间应不小于shiro的会话超时时间
        redisSessionDAO.setExpire(18000000);
        redisSessionDAO.setKeyPrefix("shiro:mysession:");
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }
    @Bean
    public CacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setKeyPrefix("shiro:mycache:");
        redisCacheManager.setRedisManager(redisManager());
        //默认全局超时属性
        redisCacheManager.setExpire(500);
        return redisCacheManager;
    }

    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
        return redisManager;
    }
}