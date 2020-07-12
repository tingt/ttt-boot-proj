package com.ttt.core.util;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author : tutingting
 * @description:
 * @date : 2019/5/10 下午3:31
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);
    private static ApplicationContext context;
    private static Set<Callback> handlers = Sets.newLinkedHashSet();

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
        for (Callback callback : handlers) {
            try {
                callback.execute(context);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public static ApplicationContext getApplicationContext() throws BeansException {
        return context;
    }

    /**
     * 注册容器加载事件
     *
     * @param callback
     */
    public static void registerCallback(Callback callback) {
        synchronized (handlers) {
            handlers.add(callback);
        }
    }

    /**
     * 获取Spring 容器中的bean
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        if (name == null || "".equals(name.trim())) {
            return null;
        }
        return context == null ? null : (T) context.getBean(name);
    }

    /**
     * 根据类型和名称获取Spring容器bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getBean(name, clazz, null);
    }

    /**
     * 根据类型和名称获取对象，支持延迟加载
     *
     * @param name
     * @param clazz
     * @param policy
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz, LazyLoadPolicy policy) {
        if (Strings.isBlank(name) || clazz == null) return null;
        if (policy != null) {
            return ProxyUtil.newProxyInstance(new BeanProxyInvocationHandler(name, clazz, policy), clazz);
        } else {
            return context.getBean(name, clazz);
        }
    }

    /**
     * 获取Spring 容器中的bean
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz, null);
    }

    /**
     * 获取Spring 容器中的bean(支持延迟加载)
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz, LazyLoadPolicy policy) {
        if (clazz == null) return null;
        if (policy != null) {
            return ProxyUtil.newProxyInstance(clazz, new BeanProxyInvocationHandler(null, clazz, policy), new ProxyUtil.CallbackFilter() {
                @Override
                public boolean accept(Method method) {
                    return !method.getDeclaringClass().equals(Object.class);
                }
            });
        } else {
            return context == null ? null : context.getBean(clazz);
        }
    }

    /**
     * 从Spring上下文获取指定类型的所有Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return context == null ? null : context.getBeansOfType(clazz);
    }

    /**
     * 延长加载Bean实例
     */
    private static class BeanProxyInvocationHandler implements InvocationHandler, Callback {

        private String beanName;
        private Class<?> beanType;
        private LazyLoadPolicy policy;
        private volatile Object target;
        private volatile BlockingQueue<Callback> queue;

        public BeanProxyInvocationHandler(String name, Class<?> clazz, LazyLoadPolicy policy) {
            beanName = name;
            beanType = clazz;
            this.policy = policy;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object target = getInstance();
            if (target != null) {
                return method.invoke(target, args);
            } else {
                registerCallback(policy);
                return policy.invoke(proxy, method, args);
            }
        }

        /**
         * 注册回调函数，在context加载完成后执行
         *
         * @param policy
         */
        private void registerCallback(LazyLoadPolicy policy) {
            if (!(policy instanceof Callback)) return;
            BlockingQueue<Callback> queue = getQueue();
            queue.offer((Callback) policy);
            SpringUtil.registerCallback(this);
        }

        /**
         * 从Spring容器延迟加载实例
         *
         * @return
         */
        private Object getInstance() {
            if (target == null) {
                synchronized (beanType) {
                    if (target == null) {
                        //上下文未初始化直接返回NULL
                        if (context == null) {
                            return null;
                        }

                        //从上下文加载bean
                        if (beanName != null) {
                            target = context.getBean(beanName, beanType);
                        } else {
                            target = context.getBean(beanType);
                        }
                    }
                }
            }

            return target;
        }

        private BlockingQueue<Callback> getQueue() {
            if (queue == null) {
                synchronized (BeanProxyInvocationHandler.class) {
                    if (queue == null) {
                        queue = new LinkedBlockingQueue<Callback>();
                    }
                }
            }
            return queue;
        }

        @Override
        public void execute(Object context) {
            BlockingQueue<Callback> queue = getQueue();
            if (queue.isEmpty()) return;

            Object target = getInstance();
            try {
                Callback[] callbacks = queue.toArray(new Callback[]{});
                for (Callback callback : callbacks) {
                    callback.execute(target);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 延迟加载策略
     */
    public static interface LazyLoadPolicy {

        /**
         * 对象尚未完成加载时，会使用此函数来代理尚未加载的类
         *
         * @param target
         * @param method
         * @param args
         * @return
         */
        Object invoke(Object target, Method method, Object[] args);
    }

    /**
     * 目标对象实例化后调用的回调函数
     */
    public static interface Callback {

        /**
         * 使用目标对象执行回调函数
         *
         * @param target
         */
        void execute(Object target);
    }

    /**
     * 终止执行并抛出一个拒绝执行的异常
     */
    public static class AbortPolicy implements LazyLoadPolicy {

        @Override
        public Object invoke(Object target, Method method, Object[] args) {
            throw new RejectedExecutionException("target:" + target + ", method:" + method.getName());
        }
    }

    /**
     * 拒绝执行, 直接返回NULL
     */
    public static class DiscardPolicy implements LazyLoadPolicy {

        @Override
        public Object invoke(Object target, Method method, Object[] args) {
            return null;
        }
    }

    /**
     * 延迟执行, 直接返回NULL, 等到对象被加载完成后再一次调用需要执行的任务（适用于对实时性要求不高的后台任务）
     */
    public static class LazyExecutePolicy implements LazyLoadPolicy, Callback {

        private Method method;
        private Object[] args;

        @Override
        public Object invoke(Object target, Method method, Object[] args) {
            if (this.method != null) throw new RuntimeException("不可重复执行");
            this.method = method;
            this.args = args;
            return null;
        }

        @Override
        public void execute(Object target) {
            if (method == null || target == null) return;
            try {
                method.invoke(target, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

