package com.ttt.core.service;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/5/11 下午3:29
 */
public class RedisCacheService {
    @Cacheable(value = "GoodsInfo#300", key = "'GoodsInfo_'+#goodsId")
    public Map<Integer, List<Long>> loadGoodsInfo(Integer goodsId) {
        //do some request
        return MapUtils.EMPTY_MAP;
    }

    @Cacheable(value = "AddressInfo#300", key = "'AddressInfo'")
    public List<Integer> loadAddress(String country,String province) {
        //do some request
        return ListUtils.EMPTY_LIST;
    }

    // 定时更新
    @CachePut(value = "AddressInfo#300", key = "'AddressInfo'")
    public List<Integer> updateAddress() {
        //do some request
        return ListUtils.EMPTY_LIST;
    }
}
