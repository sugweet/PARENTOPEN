package com.brigeintelligent.api.shiro;

import com.brigeintelligent.api.manager.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.session.Session;

import java.util.Set;

/**
 * @Description：shiro通用工具类
 * @Author：Sugweet
 * @Time：2019/4/26 11:22
 */
public class ShiroUtils {
    public static User currUser() {
        Session session = SecurityUtils.getSubject().getSession();
        if (session.getAttribute("currUser") == null) {
            throw new ShiroException("not found currUser from shiro session attribute");
        }
        return (User) session.getAttribute("currUser");
    }

    public static Boolean isAdmin() {
        Session session = SecurityUtils.getSubject().getSession();
        Object allRoles = session.getAttribute("allRoles");
        if (allRoles instanceof Set<?>) {
            Set<?> allRoles1 = (Set<?>) allRoles;
            return allRoles1.contains("admin");

        }
        return false;
    }
}
