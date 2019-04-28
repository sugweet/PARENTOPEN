package com.brigeintelligent.api.manager.service;

import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.api.manager.entity.User;

import java.util.List;
import java.util.Map;

public interface LoginService {
    User addUser(User user);

    int deleteByUsername(String username);

    User findByUsernameAndId(String username,String userId);

    List<User> findAll();

    Role addRole(Map<String,Object> map);

    User findUserByUserName(String username);
}
