package com.ttt.core.exception;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/5/14 下午4:05
 */
public class CommonException extends RuntimeException{
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
