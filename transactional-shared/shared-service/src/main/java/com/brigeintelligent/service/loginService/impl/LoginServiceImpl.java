package com.brigeintelligent.service.loginService.impl;

import com.brigeintelligent.api.manager.dao.UserDao;
import com.brigeintelligent.api.manager.entity.Permission;
import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.api.utils.IDGetGenerator;
import com.brigeintelligent.api.utils.PasswordUtils;
import com.brigeintelligent.base.BaseCode;
import com.brigeintelligent.base.BaseException;
import com.brigeintelligent.service.loginService.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Description：用户登录接口
 * @Author：Sugweet
 * @Time：2019/4/22 11:22
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public User addUser(User user) {
        if (usernameExist(user.getUsername(), user.getId())) {
            throw new BaseException(BaseCode.FAILED, "用户名已存在");
        }
        if (StringUtils.isEmpty(user.getId())) {
            // id为空说明是新增
            user.setId(IDGetGenerator.gen());
            String password = PasswordUtils.passwordEncode(user.getPassword());
            user.setPassword(password);

        } else {
            // id不为空说明是更新
            Optional<User> user1 = userDao.findById(user.getId());
            if (!user1.isPresent()) {
                throw new UnknownAccountException("用户不存在");
            }
            String password = PasswordUtils.passwordDecode(user1.get().getPassword());
            String password1 = user.getPassword();
            //判断密码是否修改
            if (!password.equals(password1)) {
                user.setPassword(PasswordUtils.passwordEncode(password1));
            } else {
                user.setPassword(user1.get().getPassword());
            }
        }
        Set<Role> roles = user.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            for (Role role : roles) {
                if (StringUtils.isEmpty(role.getRoleId())) {
                    role.setRoleId(IDGetGenerator.gen());
                }
                List<Permission> permissions = role.getPermissions();
                if (!CollectionUtils.isEmpty(permissions)) {
                    for (Permission permission : permissions) {
                        if (StringUtils.isEmpty(permission.getPermissionId())) {
                            permission.setPermissionId(IDGetGenerator.gen());
                        }
                    }
                }
            }
        }

        return userDao.save(user);
    }

    @Override
    @Transactional
    public int deleteByUsername(String username) {
        return userDao.deleteByUsername(username);
    }

    @Override
    public User findByUsernameAndId(String username, String userId) {
        User user = new User();
        // userid如果存在说明是新增校验，否则是修改校验
        if (StringUtils.isNoneEmpty(userId)) {
            user.setId(userId);
        }
        if (StringUtils.isNoneEmpty(username)) {
            user.setUsername(username);
        }

        Optional<User> one = userDao.findOne(Example.of(user));
        // one里如果有user实体就返回实体，否则返回null
        return one.orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findUserByUserName(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public Boolean usernameExist(String username, String id) {
        User user = null;
        if (StringUtils.isEmpty(id)) {
            // 如果id为null，说明是新增校验
            user = userDao.findUserByUsername(username);
            // 返回true说明用户名重复
        } else {
            // 如果id不为空，说明是修改校验
            user = userDao.findByUsernameAndIdNot(username, id);
            // 返回true说明用户名重复
        }
        return user != null;

    }

}
