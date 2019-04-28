package com.brigeintelligent.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description：shiro过滤器
 * @Author：Sugweet
 * @Time：2019/4/28 10:11
 */
public class MyAccessController extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        // 加一个集合，放行集合里所有的url,使其未登录也能访问
        List<String> noFilters = new ArrayList<>();
        noFilters.add("/api/findAll");
        if (noFilters.contains(path)) {
            return true;
        }
        // 如果是登录请求就进行登录校验
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        }
        //如果未登录，则跳转到noLogin界面
        return super.onAccessDenied(request, response);

    }

    @Override
    public boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        if (request1.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response1.setHeader("Access-Control-Allow-Origin", request1.getHeader("Origin"));
            response1.setHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,DELETE,OPTIONS");
            response1.setHeader("Access-Control-Allow-Credentials", "true");
            response1.setHeader("Access-Control-Allow-Headers", request1.getHeader("Access-Control-Request-Headers"));
            response1.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request1, response1);
    }
}
