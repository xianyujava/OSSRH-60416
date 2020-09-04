package com.easystream.spring.utils;

import com.easystream.core.EasystreamConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象
 * @param <T>
 */
public class ClientFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private EasystreamConfiguration easystreamConfiguration;

    private Class<T> interfaceClass;

    public EasystreamConfiguration getEasystreamConfiguration() {
        return easystreamConfiguration;
    }

    public void setEasystreamConfiguration(EasystreamConfiguration easystreamConfiguration) {
        this.easystreamConfiguration = easystreamConfiguration;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T getObject() throws Exception {
        if (easystreamConfiguration == null) {
            synchronized (this) {
                if (easystreamConfiguration == null) {
                    try {
                        easystreamConfiguration = applicationContext.getBean(EasystreamConfiguration.class);
                    } catch (Throwable th) {
                    }
                    if (easystreamConfiguration == null) {
                        easystreamConfiguration = EasystreamConfiguration.getDefaultConfiguration();
                    }
                }
            }
        }
        return easystreamConfiguration.createInstance(interfaceClass);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

}
