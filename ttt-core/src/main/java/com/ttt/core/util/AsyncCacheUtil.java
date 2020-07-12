package com.ttt.core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : tutingting
 * @description:异步刷新缓存
 * @date : 2020/5/11 下午3:31
 */
public class AsyncCacheUtil<K,V> implements Cache<K,V> {
    private static final Logger logger = LoggerFactory.getLogger(AsyncCacheUtil.class);

    public static final int DEFAULT_THREADS = 2;
    private static final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(DEFAULT_THREADS));

    private final Cache<K, V> cache;
    private final LoadHelper<K, V> loadHelper;
    private final V emptyV;

    public static <K, V> Cache<K, V> newSimpleCache(long size, long duration, LoadHelper<K, V> helper) {
        return newSimpleCache(size, duration, helper, true);
    }

    public static <K, V> Cache<K, V> newSimpleCache(long size, long duration, LoadHelper<K, V> helper, boolean init) {
        return new AsyncCacheUtil<>(size, duration, helper, init, null);
    }

    public static <K, V> Cache<K, V> newSimpleCache(long size, long duration, LoadHelper<K, V> helper, V emptyV) {
        return new AsyncCacheUtil<>(size, duration, helper, true, emptyV);
    }

    private AsyncCacheUtil(long size, long duration, LoadHelper<K, V> helper, boolean init, V emptyV) {
        this.loadHelper = helper;
        this.emptyV = emptyV;
        this.cache = CacheBuilder.newBuilder().maximumSize(size).refreshAfterWrite(duration, TimeUnit.SECONDS)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        try {
                            V v = loadHelper.load(key);
                            return v;
                        } finally {

                        }
                    }

                    @Override
                    public ListenableFuture<V> reload(final K key, V oldValue) throws Exception {
                        return listeningExecutorService.submit(new Callable<V>() {
                            @Override
                            public V call() throws Exception {
                                try {
                                    V load = load(key);
                                    return load;
                                } finally {

                                }
                            }
                        });
                    }
                });
        if (init) {
            logger.info("init...");
            listeningExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadHelper.load(null);
                    } finally {
                    }
                }
            });
        }
    }

    @Override
    public V getIfPresent(final Object key) {
        V v = cache.getIfPresent(key);
        if (v == null) {
            v = loadHelper.load((K) key);
//            logger.warn("remote get key: {}, value: {}", key, v);
            // 缓存空值
            if (v == null) {
                if (emptyV != null) {
                    put((K) key, emptyV);
                }
            } else {
                put((K) key, v);
            }
        }
        return v;
    }

    @Override
    public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
        return cache.get(key, valueLoader);
    }

    @Override
    public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
        return cache.getAllPresent(keys);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        cache.putAll(m);
    }

    @Override
    public void invalidate(Object key) {
        cache.invalidate(key);
    }

    @Override
    public void invalidateAll(Iterable<?> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public CacheStats stats() {
        return cache.stats();
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return cache.asMap();
    }

    @Override
    public void cleanUp() {
        cache.cleanUp();
    }

    public interface LoadHelper<K, V> {
        V load(K key);
    }
}
