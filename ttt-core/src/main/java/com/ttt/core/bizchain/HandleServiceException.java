package com.ttt.core.bizchain;

/**
 * @Author: fengmeng
 * @Date: 2019/3/12
 * @Description: 订单服务异常
 */
public class HandleServiceException extends RuntimeException {

    private static final long serialVersionUID = -183070443245145777L;

    private static final Integer SYSTEM_BUSINESS = 5000;

    /**
     * 错误编号
     */
    private Integer errorCode;


    public HandleServiceException() {
        super();
    }

    public HandleServiceException(Integer errorCode) {
        this(errorCode, null);
    }

    public HandleServiceException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HandleServiceException(Integer errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;

    }

    public HandleServiceException(String message) {
        super(message);
    }

    public HandleServiceException(Throwable cause) {
        super(cause);
    }

    public HandleServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        if (null == errorCode) {
            //通用异常，正常不会出现的异常
            this.errorCode = SYSTEM_BUSINESS;
        } else {
            this.errorCode = errorCode;
        }
    }
}
