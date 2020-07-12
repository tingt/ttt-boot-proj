package com.ttt.core.bizchain;

/**
 * @Author: fengmeng
 * @Date: 2019/3/12
 * @Description: BizHandle接口, 业务处理核心接口，每个BizHandle仅仅处理一件事情，保存简单性
 * * 处理结果存放在OrderContext中，OrderContext保存了业务链执行过程中的所有状态信息
 */
public interface BizHandle<T> {

    /**
     * 业务处理方法
     * @param context
     * @return 流程正常结束时，返回值为false，会正常提前终止BizChain调用，如果异常终止流程请抛出OrderServiceException
     * 调用正常结束返回true
     * @throws HandleServiceException
     */
    boolean handle(T context) throws HandleServiceException;


    /**
     * BizHandle是否触发执行，默认为执行
     *
     * @param content 上下文
     * @return 是否执行
     */
    default boolean excCondition(T content) {
        return true;
    }


}

