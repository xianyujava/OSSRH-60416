package com.easystream.spring.proxy;

import com.easystream.core.EasystreamConfiguration;
import com.easystream.spring.reflection.EasystreamMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName InterfaceProxyHandler
 * @Description: 动态代理，需要注意的是，这里用到的是JDK自带的动态代理，代理对象只能是接口，不能是类
 * @Author soft001
 * @Date 2020/8/31
 **/
public class InterfaceProxyHandler<T> implements InvocationHandler {

    private EasystreamConfiguration configuration;

    private ProxyFactory proxyFactory;

    private Class<T> interfaceClass;

    private Map<Method, EasystreamMethod> forestMethodMap = new HashMap<Method, EasystreamMethod>();


    private List<Annotation> baseAnnotations = new LinkedList<>();


    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public InterfaceProxyHandler(EasystreamConfiguration configuration, ProxyFactory proxyFactory, Class<T> interfaceClass) {
        this.configuration = configuration;
        this.proxyFactory = proxyFactory;
        this.interfaceClass = interfaceClass;
        initMethods();
    }

    private void initMethods() {
        Method[] methods = interfaceClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            EasystreamMethod forestMethod = new EasystreamMethod(this, configuration, method);
            forestMethodMap.put(method, forestMethod);
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
//        if (methodName.equals("getProxyHandler") && (args == null || args.length == 0)) {
//            return this;
//        }
        if (methodName.equals("toString") && (args == null || args.length == 0)) {
            return "{Easystream Proxy Object of " + interfaceClass.getName() + "}";
        }
        if (methodName.equals("equals") && (args != null && args.length == 1)) {
            Object obj = args[0];
            if (Proxy.isProxyClass(obj.getClass())) {
                InvocationHandler h1 = Proxy.getInvocationHandler(proxy);
                InvocationHandler h2 = Proxy.getInvocationHandler(obj);
                return h1.equals(h2);
            }
            return false;
        }
        EasystreamMethod forestMethod = forestMethodMap.get(method);
        return forestMethod.invoke(args);
    }


}
