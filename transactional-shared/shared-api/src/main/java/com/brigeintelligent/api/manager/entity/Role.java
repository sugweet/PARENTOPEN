package com.brigeintelligent.api.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Description：角色实体类
 * @Author：Sugweet
 * @Time：2019/4/18 10:11
 */
@Data
@Entity
@Table(name = "sys_role")
public class Role implements Serializable {

    @Id
    @Column(name = "role_id",length = 36)
    private String roleId;
    @Column(name = "role_name",length = 64)
    private String roleName;
    @Column(name = "description", length = 128)
    private String description;
    //角色和权限是多对多关系，MERGE表示增删用户时，对角色只增不删
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",
    joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions;
}
