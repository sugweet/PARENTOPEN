package com.brigeintelligent.api.shiro.realm;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @Description：自定义用户名/密码的认证机制
 * @Author：Sugweet
 * @Time：2019/4/25 16:26
 */
public class ShiroToken extends UsernamePasswordToken {
    public ShiroToken() {
    }

    public ShiroToken(String username, char[] password) {
        super(username, password);
    }

    public ShiroToken(String username, String password) {
        super(username, password);
    }

    public ShiroToken(String username, char[] password, String host) {
        super(username, password, host);
    }

    public ShiroToken(String username, String password, String host) {
        super(username, password, host);
    }

    public ShiroToken(String username, char[] password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public ShiroToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public ShiroToken(String username, char[] password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public ShiroToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }
}
