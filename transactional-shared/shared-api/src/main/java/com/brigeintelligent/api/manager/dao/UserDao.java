package com.brigeintelligent.api.manager.dao;

import com.brigeintelligent.api.manager.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User,String> {
    User findUserByUsername(String username);

    int deleteByUsername(String username);
}
