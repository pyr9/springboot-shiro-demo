package com.pyr.shiro.demo1.config;

import com.pyr.shiro.demo1.AuthRealm;
import com.pyr.shiro.demo1.CredentialMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfiguration {

    //ShiroFilter过滤所有请求
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);

        bean.setLoginUrl("/login"); // 设置登录url
        bean.setSuccessUrl("/index");// 设置登录成功后，跳转的url
        bean.setUnauthorizedUrl("/unauthorized"); // 设置没有访问权限后后，跳转的url

        // 配置请求该怎么拦截
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 需要登录
        filterChainDefinitionMap.put("/index", DefaultFilter.authc.name());
        // 不需要登录
        filterChainDefinitionMap.put("/login", DefaultFilter.anon.name());
        filterChainDefinitionMap.put("/loginUser", DefaultFilter.anon.name());

        filterChainDefinitionMap.put("/admin", "roles[admin]");

        // 其他接口需要检验用户是否登录
        filterChainDefinitionMap.put("/**", DefaultFilter.user.name());

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return bean;
    }

    // 创建安全管理器
    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    // 创建自定义Realm
    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher matcher) {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher() {
        return new CredentialMatcher();
    }

    /**
     * shiro和spring的配置
     * 设置spring使用的securityManager是我们自定义的securityManager
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
