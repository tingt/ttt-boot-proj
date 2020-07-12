package com.ttt.core.bizchain;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @Author: fengmeng
 * @Date: 2019/3/12
 * @Description: chain，	通过调用handles达到目的,同时BizChain也可以作为一个BizHandle放入到其它的BizChain中
 * * 同时支持耗时统计
 */
public class BizChain<T> implements BizHandle<T> {

    private static final Logger logger = LoggerFactory.getLogger(BizChain.class);
    protected List<BizHandle<T>> handles;
    /**
     * 是否记录handle耗时开关
     */
    private boolean logCostTime = true;

    public boolean execute(T context) throws HandleServiceException {
        boolean result = false;
        for (BizHandle<T> handler : handles) {
            if (!handler.excCondition(context)) {
                continue;
            }
            long start = System.currentTimeMillis();
            boolean logContext = false;
            if (context instanceof BaseContextDTO) {
                String className = ((BaseContextDTO) context).getDebugHandleClassName();
                if (className != null && className.equals(handler.getClass().getSimpleName())) {
                    logContext = true;
                }
            }
            if (logContext) {
                logger.error("before execute handle,context json  " + JSON.toJSONString(context));
            }

            result = handler.handle(context);
            if (logCostTime && context instanceof BaseContextDTO) {
                if (((BaseContextDTO) context).getTimeUseDetial() == null) {
                    ((BaseContextDTO) context).setTimeUseDetial(new HashMap<>(15));
                }
                ((BaseContextDTO) context).getTimeUseDetial().put(handler.getClass().getSimpleName(), System.currentTimeMillis() - start);
            }

            if (logContext) {
                logger.error("after execute handle,context json  " + JSON.toJSONString(context));
            }
        }
        return result;
    }

    @Override
    public boolean handle(T context) throws HandleServiceException {
        if (excCondition(context)) {
            return this.execute(context);
        }
        return true;
    }

    public void setHandles(List<BizHandle<T>> handles) {
        this.handles = handles;
    }

    public void setLogCostTime(boolean logCostTime) {
        this.logCostTime = logCostTime;
    }
}
