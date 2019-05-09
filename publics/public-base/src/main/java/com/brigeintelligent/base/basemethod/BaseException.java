package com.brigeintelligent.base.basemethod;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description：基础异常处理类
 * @Author：Sugweet
 * @Time：2019/4/28 10:47
 */
@Getter
@Setter
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -4614067754602422487L;

    private int code;
    private String msg;

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(int code,Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = cause.getMessage();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"-"+code;
    }
}
