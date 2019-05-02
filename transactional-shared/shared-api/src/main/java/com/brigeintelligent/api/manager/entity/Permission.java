package com.brigeintelligent.api.manager.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description：权限实体类
 * @Author：Sugweet
 * @Time：2019/4/18 14:05
 */
@Data
@Entity
@Table(name = "sys_permission")
@ApiModel(value = "Permission实体",description = "权限实体对象")
public class Permission implements Serializable {
    private static final long serialVersionUID = 5305012470564269093L;
    @Id
    @Column(name = "permission_id",length = 36)
    @ApiModelProperty(value = "权限Id",name = "permissionId",required = false)
    private String permissionId;
    @Column(name = "permission",length = 128)
    @ApiModelProperty(value = "权限",name = "permission",required = false)
    private String permission;
    @Column(name = "description",length = 128)
    @ApiModelProperty(value = "权限描述",name = "description",required = false)
    private String description;
}
