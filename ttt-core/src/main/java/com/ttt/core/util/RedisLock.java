/*
package com.ttt.core.util;

import com.ttt.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
import redis.clients.jedis.Jedis;

*/
/**
 * @author : tutingting
 * @description:
 * @date : 2020/5/14 下午3:57
 *//*

public class RedisLock {

    private StringRedisTemplate redisTemplate;
    private String key;
    private long expireTime;
    private TimeUnit timeUnit;
    private boolean isLock;
    private String value;

    public static final long DEFAULT_TRY_INTERVAL_MILLIS = 1000L;
    public static final int DEFAULT_MAX_TRY_COUNT = 3;
    */
/**
     * OC主流程添加redis锁前缀
     *//*

    public static final String COMMON_KEY_PREFIX = "oc_api_lock_";
    */
/**
     * OC人工拆单接口添加redis锁前缀
     *//*

    public static final String RESPLIT_KEY_PREFIX = "re_split_waybill_lock_";

    */
/**
     * 设置redis锁的过期时间60s
     *//*

    public static final int DEFAULT_EXPIRE_TIME_SECONDS = 60;
    */
/**
     * 设置redis锁并设置锁超时成功则返回"1",否则返回"0"
     *//*

    private static final String SUCCESS = "1";

    Logger logger = LoggerFactory.getLogger(RedisLock.class);

    */
/**
     * 设置加锁时同时设置过期时间的lua脚本
     *//*

    private static String LUA_SCRIPT_SET_KEY_AND_EXPIRE_TIME;
    */
/**
     * 删除锁时判断是否是之前获取锁的线程
     *//*

    private static String LUA_SCRIPT_REMOVE_KEY;

    static {
        LUA_SCRIPT_SET_KEY_AND_EXPIRE_TIME = "if redis.call('setNx',KEYS[1],ARGV[1])==1 then\n" +
                "return redis.call('expire',KEYS[1],ARGV[2])\n" +
                "else\n" +
                "return 0\n" +
                "end\n";

        LUA_SCRIPT_REMOVE_KEY = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                "then return redis.call('del', KEYS[1]) else return 0 end";
    }

    public RedisLock(StringRedisTemplate redisTemplate, String key, long expireTime, TimeUnit timeUnit) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }
    public RedisLock(StringRedisTemplate redisTemplate, String key,String value, long expireTime, TimeUnit timeUnit) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }

    public interface LockAction<T> {
        T execute() throws CommonException;
    }
*/
/**
    /**
     *
     * @param lockAction
     * @param redisTemplate
     * @param key
     * @param timeout
     * @return
     *//*

    public static <T> T doActionUseLockWithLua(LockAction<T> lockAction, StringRedisTemplate redisTemplate, String key, String value, int timeout) {
        RedisLock redisLock = new RedisLock(redisTemplate, key, value, timeout, TimeUnit.SECONDS);
        try {
            if (redisLock.tryLockWithLua()) {
                return lockAction.execute();
            } else {
                throw new CommonException();
            }
        }finally {
            if (redisLock.isLock()) {
                redisLock.unLockWithLua();
            }
        }
    }

    */
/**
     * 非可重入redis互斥锁,通过lua脚本实现同时设定值以及过期时间
     * redis高版本已经实现同时设定值以及过期时间，但是OC使用的版本太低，升级redis同时需要要将spring升至5，改动很大
     * @return 是否加锁成功
     *//*

    private boolean tryLockWithLua() {
        //是否打开redis锁
        boolean redisLockSwitch = true;
        if(redisLockSwitch){
            // 设置为过期时间
            final String expireTimeStr = String.valueOf(expireTime);
            RedisCallback<String> callback = new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    return ((Jedis) nativeConnection).eval(LUA_SCRIPT_SET_KEY_AND_EXPIRE_TIME, 1, key, value, expireTimeStr).toString();
                }
            };
            String success = redisTemplate.execute(callback);
            if (SUCCESS.equals(success)) {
                isLock = true;
                return true;
            }
            return false;
        }
        return true;
    }

    */
/**
     * 这里简单实现解锁，非获取锁的线程也能直接解锁，一定要在获取锁成功才能调这个方法。
     *//*

    private void unLockWithLua() {
        RedisCallback<String> callback = new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                return ((RedisProperties.Jedis) nativeConnection).eval(LUA_SCRIPT_REMOVE_KEY, 1, key, value).toString();
            }
        };
        String success = redisTemplate.execute(callback);
        if(SUCCESS.equals(success)){
            isLock = false;
        }
    }

    public boolean isLock() {
        return isLock;
    }

}
*/
