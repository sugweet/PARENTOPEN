package com.brigeintelligent.api.shiro;

import com.brigeintelligent.api.manager.dao.UserDao;
import com.brigeintelligent.api.manager.entity.Permission;
import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.base.baseutils.PasswordUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description：shiro自定义认证
 * @Author：Sugweet
 * @Time：2019/4/22 10:54
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserDao userDao;

    /**
     * 角色和对应权限添加
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        // 根据用户名查询用户
        User user = userDao.findUserByUsernameAndId(username, null);
        // 添加角色和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            // 添加角色
            info.addRole(role.getRoleName());
            for (Permission permission : role.getPermissions()) {
                // 添加权限
                info.addStringPermission(permission.getPermission());
            }
        }
        return info;
    }

    /**
     * 用户认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取token
        ShiroToken shiroToken = (ShiroToken) token;
        // 获取用户名
        String username = shiroToken.getUsername();
        // 根据用户实名查询用户
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("用户名不存在");
        }
        // 用户存在就判断密码是否正确
        // 数据库解密后的密码
        String password = PasswordUtils.passwordDecode(user.getPassword());
        // 页面输入的密码
        String password2 = new String(shiroToken.getPassword());
        if (!password.equals(password2)) {
            throw new IncorrectCredentialsException("密码错误");
        }
        //判断用户是否被锁定
        if (user.getLocked().equals(1)) { //1表示已锁定
            throw new LockedAccountException("用户已被锁定，不可用");
        }
        // 定义需要进行返回的操作数据信息项，返回的认证信息使用的应该是密文
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());
        // 在认证完成之后可以直接去的用户所需要的信息内容，保存在Session中。
        SecurityUtils.getSubject().getSession().setAttribute("currUser",user);
        //获取角色
        Set<Role> roles = user.getRoles();
        Set<String> allRoles = new HashSet<>();
        Set<String> allPermissions = new HashSet<>();
        for (Role role : roles) {
            allRoles.add(role.getRoleName());
            // 获取权限
            List<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                allPermissions.add(permission.getPermission());
            }
        }
        SecurityUtils.getSubject().getSession().setAttribute("allRoles", allRoles);
        SecurityUtils.getSubject().getSession().setAttribute("allPermissions", allPermissions);

        return info;
    }

}
