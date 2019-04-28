package com.brigeintelligent.shiro;

import com.brigeintelligent.api.shiro.realm.CustomerCredentialMatcher;
import com.brigeintelligent.api.shiro.realm.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description：shiro的核心配置
 * @Author：Sugweet
 * @Time：2019/4/26 13:43
 */
@Configuration
public class ShiroConfig {

    @Bean
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
        //必须设置securityManager
        filterBean.setSecurityManager(securityManager);
        //设置登录页，如果不设置默认寻找web工程根目录下的login.jsp页面
        filterBean.setLoginUrl("/api/noLogin");
        // 登陆成功后跳转的链接
        filterBean.setSuccessUrl("/api/login");
        // 未授权页面
        filterBean.setUnauthorizedUrl("/api/403");

        // 配置拦截
        Map<String, Filter> filters = new LinkedHashMap<>();
        Filter accessControlFilter = new MyAccessController();
        filters.put("authc", accessControlFilter);
        filterBean.setFilters(filters);

        Map<String, String> map = new HashMap<>();
        // 登出
        map.put("/api/logout", "logout");

        // 登录页面放行
        map.put("/api/login", "anon");
        // 对所有用户认证
        map.put("/**", "authc");

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
