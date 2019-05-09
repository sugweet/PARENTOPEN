package com.brigeintelligent.api.manager.dao;

import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.base.basemethod.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User,String> {

    User findUserByUsername(String username);

    User findUserByUsernameAndId(String username, String id);

    User findByUsernameAndIdNot(String username, String id);

    int deleteByUsername(String username);



}
