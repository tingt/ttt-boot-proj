package com.ttt.core.bizchain;

import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: fengmeng
 * @Date: 2019/3/12
 * @Description: 支持数据库事务的业务chain，通过调用handles达到目的
 */
@Transactional(rollbackFor = Exception.class)
public class TxBizChain<T> extends BizChain<T> {

    @Override
    public boolean handle(T t) {
        return super.handle(t);
    }

    @Override
    public boolean execute(T t) {
        return super.execute(t);
    }

}

