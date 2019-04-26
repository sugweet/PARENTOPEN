package com.brigeintelligent.api.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @Description：加密密码的验证
 * @Author：Sugweet
 * @Time：2019/4/26 11:39
 */
public class CustomerCredentialMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取页面输入的密码
        String pagePassword = super.toString(token.getCredentials());
        // 获取数据库中的密码
        Object dataPassword = super.getCredentials(info);
        String password = super.toString(dataPassword);

        //密码验证
        return super.equals(pagePassword,password);
    }
}
