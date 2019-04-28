package com.brigeintelligent.api.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @Description：用户实体类
 * @Author：Sugweet
 * @Time：2019/4/18 10:01
 */
@Data
@Entity
@Table(name = "sys_user")
public class User implements Serializable {
    @Id
    @Column(name = "user_id",length = 36)
    private String id;
    @Column(name = "user_name",length = 64)
    private String username;
    @Column(name = "password",length = 64)
    private String password;
    @Column(name = "locked",length = 1)
    private Integer locked; //用户是否被锁定
    //用户和角色是多对多关系，MERGE表示增删用户时，对角色只增不删
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;
}
