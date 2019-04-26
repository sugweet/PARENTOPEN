package com.brigeintelligent.api.manager.service.impl;

import com.brigeintelligent.api.manager.dao.RoleDao;
import com.brigeintelligent.api.manager.dao.UserDao;
import com.brigeintelligent.api.manager.entity.Permission;
import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.api.manager.service.LoginService;
import com.brigeintelligent.api.utils.IDGetGenerator;
import com.brigeintelligent.api.utils.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description：用户登录接口
 * @Author：Sugweet
 * @Time：2019/4/22 11:22
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional
    public User addUser(User user) {
        if (StringUtils.isEmpty(user.getId())) {
            // id为空说明是新增
            user.setId(IDGetGenerator.gen());
            String password = PasswordUtils.passwordEncode(user.getPassword());
            user.setPassword(password);

        } else {
            // id不为空说明是更新
            Optional<User> user1 = userDao.findById(user.getId());
            if (!user1.isPresent()) {
                throw new RuntimeException("用户不存在");
            }
            String password = PasswordUtils.passwordDecode(user1.get().getPassword());
            String password1 = user.getPassword();
            if (!password.equals(password1)) {
                user.setPassword(PasswordUtils.passwordEncode(password1));
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
    @Transactional
    @SuppressWarnings("unchecked")
    public Role addRole(Map<String, Object> map) {
        Role role = new Role();
        role.setRoleId(IDGetGenerator.gen());
        role.setRoleName(map.get("roleName").toString());
        role.setDescription(map.get("description").toString());
        List<Permission> permissions = (List<Permission>) map.get("permissions");
        role.setPermissions(permissions);
        return roleDao.save(role);
    }

}
