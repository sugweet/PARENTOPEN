package com.brigeintelligent.shiro;

import com.brigeintelligent.api.shiro.CustomerCredentialMatcher;
import com.brigeintelligent.api.shiro.MyRealm;
import com.brigeintelligent.api.shiro.ShiroSessionListener;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @Description：shiro的核心配置
 * @Author：Sugweet
 * @Time：2019/4/26 13:43
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置自定义认证
     * @return
     */
    @Bean
    public MyRealm getRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(new CustomerCredentialMatcher());
        return myRealm;
    }

    /**
     * 配置session监听
     * @return
     */
    @Bean
    public ShiroSessionListener getSessionListener() {
        return new ShiroSessionListener();
    }

    /**
     * 配置sessionId生成器
     * @return
     */
    @Bean
    public SessionIdGenerator getSessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 配置EhCache缓存
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehCacheManager;

    }

    /**
     *  SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDao = new EnterpriseCacheSessionDAO();
        // 使用缓存
        sessionDao.setCacheManager(ehCacheManager());
        // session缓存的名字
        sessionDao.setActiveSessionsCacheName("shiro-activeSessionCache");
        // 设置sessionId生成器
        sessionDao.setSessionIdGenerator(getSessionIdGenerator());
        return sessionDao;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置会话管理器，设定会话超时及保存
     * @return
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(getSessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(ehCacheManager());
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        //全局会话超时时间（单位毫秒），默认30分钟
        sessionManager.setGlobalSessionTimeout(30*60*1000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(60*60*1000);

        return sessionManager;

    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getRealm());
        //配置 ehcache缓存管理器
        securityManager.setCacheManager(ehCacheManager());

        //配置自定义session管理，使用ehcache 或redis
        securityManager.setSessionManager(sessionManager());

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
        filterBean.setSuccessUrl("/swagger");
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
        //map.put("/api/addUser", "anon");
        // swagger页面放行
        map.put("/swagger/**", "anon");
        // 对所有用户认证
        map.put("/api/**", "authc");

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
