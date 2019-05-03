package com.brigeintelligent.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description：基础响应结果处理
 * @Author：Sugweet
 * @Time：2019/4/28 9:15
 */
@Getter
@Setter
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -8565389322838198656L;

    private Boolean flag = false;
    private Integer code = BaseCode.FAILED;
    private Object msg = null;

    public BaseResponse() {
    }

    public BaseResponse(Integer code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(Boolean flag, Integer code, Object msg) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }
}
