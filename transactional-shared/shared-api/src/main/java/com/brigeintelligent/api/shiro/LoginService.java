package com.brigeintelligent.api.shiro;

import com.brigeintelligent.api.manager.entity.User;

import java.util.List;

public interface LoginService {
    User addUser(User user);

    int deleteByUsername(String username);

    User findByUsernameAndId(String username,String userId);

    List<User> findAll();

    User findUserByUserName(String username);

    Boolean usernameExist(String username,String id);
}
