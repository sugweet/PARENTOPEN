package com.brigeintelligent.shiro;

import com.brigeintelligent.api.shiro.realm.CustomerCredentialMatcher;
import com.brigeintelligent.api.shiro.realm.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description：shiro的核心配置
 * @Author：Sugweet
 * @Time：2019/4/26 13:43
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "myRealm")
    public MyRealm getRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(new CustomerCredentialMatcher());
        return myRealm;
    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getRealm());
        return securityManager;
    }

    /**
     * 过滤工厂，设置过滤条件和跳转条件
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterBean = new ShiroFilterFactoryBean();
        filterBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        // 登出
        map.put("/logout", "logout");
        // 对所有用户认证
        map.put("/**", "authc");
        map.put("/api/shiro/addUser", "anon");
        // 登录
        filterBean.setLoginUrl("/api/shiro/login");
        // 首页
        filterBean.setSuccessUrl("/index");
        // 错误页面，认证不通过跳转
        filterBean.setUnauthorizedUrl("/error");
        filterBean.setFilterChainDefinitionMap(map);
        return filterBean;
    }

    /**
     * 加入注解使用，否则不生效
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor sourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

}
