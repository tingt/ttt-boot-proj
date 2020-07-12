package com.ttt.core.bizchain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: fengmeng
 * @Date: 2019/3/13
 * @Description: 基础上下文类
 */
@Getter
@Setter
public class BaseContextDTO extends BaseDTO {

    private static final long serialVersionUID = 1821237187458450018L;

    private transient Map<String, Long> timeUseDetial;

    /**
     * 是否debug模式（打印日志开关）
     */
    private Boolean debugMode;

    /**
     * 开启debug模式的handle简单类名
     */
    private String debugHandleClassName;

    @Override
    public String toString() {
        return "BaseContextDTO{" +
                "timeUseDetial=" + timeUseDetial +
                ", debugMode=" + debugMode +
                ", debugHandleClassName='" + debugHandleClassName + '\'' +
                '}';
    }
}
