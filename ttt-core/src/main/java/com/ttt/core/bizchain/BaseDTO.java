package com.ttt.core.bizchain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: fengmeng
 * @Date: 2019/3/13
 * @Description: 基础上下文类
 */
@Getter
@Setter
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 6581029953195073637L;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 发起请求的用户IP
     */
    private String userIp;

    /**
     * 操作者ID
     */
    private Long operatorId;

    /**
     * 调用来源
     */
    private String invokeBy;

    @Override
    public String toString() {
        return "BaseDTO{" +
                "clientIp='" + clientIp + '\'' +
                ", userIp='" + userIp + '\'' +
                ", operatorId=" + operatorId +
                ", invokeBy='" + invokeBy + '\'' +
                '}';
    }

}
