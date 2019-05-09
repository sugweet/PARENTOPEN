package com.brigeintelligent.api.manager.dao;

import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.base.basemethod.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends BaseDao<Role,String> {
    int deleteRoleByRoleName(String roleName);
}
