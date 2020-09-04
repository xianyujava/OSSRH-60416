package com.easystream.spring.reflection;

import com.easystream.core.EasystreamConfiguration;
import com.easystream.core.annotation.RequestType;
import com.easystream.core.stream.CameraPojo;
import com.easystream.core.stream.cv.videoimageshot.exception.StreamRuntimeException;
import com.easystream.spring.proxy.InterfaceProxyHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @ClassName EasystreamMethod
 * @Description: 反射接口中方法
 * @Author soft001
 * @Date 2020/9/1
 **/
public class EasystreamMethod<T> {
    private final EasystreamConfiguration configuration;
    private final InterfaceProxyHandler interfaceProxyHandler;
    //private InterceptorFactory interceptorFactory;
    private final Method method;
    private Method handlerMethod = null;
    private MethodHandler methodHandler = null;

    public EasystreamMethod(InterfaceProxyHandler interfaceProxyHandler, EasystreamConfiguration configuration, Method method) {
        this.interfaceProxyHandler = interfaceProxyHandler;
        this.configuration = configuration;
        this.method = method;
        //this.interceptorFactory = configuration.getInterceptorFactory();
        //processBaseProperties();
        processInterfaceMethods();
    }

    /**
     * 处理接口中定义的方法
     */
    private void processInterfaceMethods() {
//        Annotation[] annotations = method.getAnnotations();
        Class[] paramTypes = method.getParameterTypes();
//        Type[] genericParamTypes = method.getGenericParameterTypes();
//        TypeVariable<Method>[] typeVariables = method.getTypeParameters();
//        Annotation[][] paramAnns = method.getParameterAnnotations();
//        Parameter[] parameters = method.getParameters();
        if (method != null && method.isAnnotationPresent(RequestType.class)) {
            RequestType annotaion = method.getAnnotation(RequestType.class);
            String methodName = annotaion.value();
            methodHandler = new MethodHandler(configuration);
            try {
                handlerMethod = methodHandler.getClass().getDeclaredMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                throw new StreamRuntimeException(e);
            }
        }
    }

    /**
     * 调用方法
     *
     * @param args
     * @return
     */
    public Object invoke(Object[] args) {
        String result="";
        try {
           result= (String) handlerMethod.invoke(methodHandler,args);
        } catch (IllegalAccessException e) {
            throw new StreamRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new StreamRuntimeException(e);
        }
//        CameraPojo pojo = (CameraPojo) args[0];
        return result;
    }
}
