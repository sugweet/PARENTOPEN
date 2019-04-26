package com.brigeintelligent.api.manager.entity;

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
public class Permission implements Serializable {
    @Id
    @Column(name = "permission_id",length = 36)
    private String permissionId;
    @Column(name = "permission",length = 128)
    private String permission;
    @Column(name = "description",length = 128)
    private String description;
}
