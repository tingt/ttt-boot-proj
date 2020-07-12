package com.ttt.core.util;
import com.google.common.collect.Lists;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author : tutingting
 * @description: 代理类工具
 * @date : 2019/5/10 下午3:32
 */
public class ProxyUtil {
    /**
     * 生成代理类
     *
     * @param invocationHandler
     * @param types
     * @param <T>
     * @return
     */
    public static <T> T newProxyInstance(final InvocationHandler invocationHandler, Class<?>... types) {
        Enhancer enhancer = new Enhancer();

        if (types!=null  && types.length>0) {
            List<Class<?>> interfaces = Lists.newArrayList();

            for (Class<?> type : types) {
                if (type.isInterface()) {
                    interfaces.add(type);
                } else {
                    enhancer.setSuperclass(type);
                }
            }

            if (interfaces.size() > 0) {
                enhancer.setInterfaces(interfaces.toArray(new Class[0]));
            }
        }

        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> invocationHandler.invoke(o, method, objects));

        return (T) enhancer.create();
    }

    /**
     * 为对象生成代理对象
     *
     * @param invocationHandler
     * @param filter
     * @param <T>
     * @return
     */
    public static <T> T newProxyInstance(Class<?> targetClass, final InvocationHandler invocationHandler, final CallbackFilter filter) {
        if (targetClass == null) return null;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new SimpleMethodInterceptor(invocationHandler, filter));

        return (T) enhancer.create();
    }

    /**
     * 默认拦截器,解决序列化问题
     */
    private static class SimpleMethodInterceptor implements MethodInterceptor, Serializable {

        private transient InvocationHandler invocationHandler;
        private transient CallbackFilter filter;

        public SimpleMethodInterceptor(InvocationHandler invocationHandler, CallbackFilter filter) {
            this.invocationHandler = invocationHandler;
            this.filter = filter;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if (filter.accept(method)) {
                return invocationHandler.invoke(o, method, objects);
            } else {
                return methodProxy.invokeSuper(o, objects);
            }
        }
    }

    /**
     * 回调函数过滤器，过滤掉非代理的函数
     */
    public static interface CallbackFilter {
        boolean accept(Method method);
    }
}

