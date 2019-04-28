package com.brigeintelligent.api.manager.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "User实体",description = "用户实体对象")
public class User implements Serializable {
    private static final long serialVersionUID = -4744800280832599604L;
    @Id
    @Column(name = "user_id",length = 36)
    @ApiModelProperty(value = "用户ID",name = "id",required = true)
    private String id;
    @Column(name = "user_name",length = 64)
    @ApiModelProperty(value = "用户名",name = "username",required = true)
    private String username;
    @Column(name = "password",length = 64)
    @ApiModelProperty(value = "密码",name = "password",required = true)
    private String password;
    @Column(name = "locked",length = 1)
    @ApiModelProperty(value = "用户是否被锁定",name = "locked",required = true)
    private Integer locked; //用户是否被锁定
    //用户和角色是多对多关系，MERGE表示增删用户时，对角色只增不删
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @ApiModelProperty(value = "用户对应角色集合",name = "roles",required = true)
    private Set<Role> roles;
}
