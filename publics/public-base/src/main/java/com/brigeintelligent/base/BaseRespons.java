package com.brigeintelligent.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description：基础响应结果处理
 * @Author：Sugweet
 * @Time：2019/4/28 9:15
 */
@Getter
@Setter
public class BaseRespons implements Serializable {

    private static final long serialVersionUID = -8565389322838198656L;

    private Integer code = BaseCode.FAILED;
    private String msg = null;
    private Map<String,Object> result = new HashMap<>();
}
