package com.brigeintelligent.api.manager.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Role实体",description = "角色实体对象")
public class Role implements Serializable {

    private static final long serialVersionUID = -9029430597698064488L;
    @Id
    @Column(name = "role_id",length = 36)
    @ApiModelProperty(value = "角色ID",name = "roleId",required = false)
    private String roleId;
    @Column(name = "role_name",length = 64)
    @ApiModelProperty(value = "角色名",name = "roleName",required = false)
    private String roleName;
    @Column(name = "description", length = 128)
    @ApiModelProperty(value = "描述",name = "description",required = false)
    private String description;
    //角色和权限是多对多关系，MERGE表示增删用户时，对角色只增不删
    @ApiModelProperty(value = "角色下的权限",name = "permissions",required = false)
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",
    joinColumns = {@JoinColumn(name = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions;
}
