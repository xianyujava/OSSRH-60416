package com.easystream.spring.proxy;

import com.easystream.core.EasystreamConfiguration;

import java.lang.reflect.Proxy;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2016-03-25 18:17
 */
public class ProxyFactory<T> {

    private EasystreamConfiguration configuration;
    private Class<T> interfaceClass;

    public ProxyFactory(EasystreamConfiguration configuration, Class<T> interfaceClass) {
        this.configuration = configuration;
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T createInstance() {
//        T instance = (T) configuration.getInstanceCache().get(interfaceClass);
//        boolean cacheEnabled = configuration.isCacheEnabled();
//        if (cacheEnabled && instance != null) {
//            return instance;
//        }
//        synchronized (configuration.getInstanceCache()) {
//            instance = (T) configuration.getInstanceCache().get(interfaceClass);
//            if (cacheEnabled && instance != null) {
//                return instance;
//            }
             //这里主要是创建接口对应的实例，便于注入到spring容器中
            InterfaceProxyHandler<T> interfaceProxyHandler = new InterfaceProxyHandler<T>(configuration, this, interfaceClass);
           T instance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, interfaceProxyHandler);
//            if (cacheEnabled) {
//                configuration.getInstanceCache().put(interfaceClass, instance);
//            }
            return instance;
        }
//    }

}
