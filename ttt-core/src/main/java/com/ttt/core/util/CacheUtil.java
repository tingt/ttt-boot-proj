package com.ttt.core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ttt.core.entity.Order;
import com.ttt.core.service.OrderService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : tutingting
 * @description:使用guava 缓存
 * @date : 2020/5/11 下午3:32
 */
public class CacheUtil {
    private static final Cache<Integer, Order> ORDER_CACHE = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(5, TimeUnit.MINUTES).build();
    public static final Order ORDER = new Order();

    public static Order getOrder(final Integer id){
        try {
            return ORDER_CACHE.get(id, () -> {
                Order order = SpringUtil.getBean(OrderService.class).getById(id);
                return order == null ? ORDER : order;
            });

        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
