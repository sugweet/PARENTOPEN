package com.brigeintelligent.api.manager.entity;

import com.brigeintelligent.base.BaseRespons;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description：登录响应实体
 * @Author：Sugweet
 * @Time：2019/4/28 9:34
 */
@Setter
@Getter
public class LoginResp extends BaseRespons {
    private static final long serialVersionUID = 4215053745205899090L;
    private User user;
}
