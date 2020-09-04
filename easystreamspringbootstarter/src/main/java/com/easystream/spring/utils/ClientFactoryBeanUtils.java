package com.easystream.spring.utils;


import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * @ClassName ClientFactoryBeanUtils
 * @Description: 接口bean工厂工具类
 * @Author soft001
 * @Date 2020/8/30
 **/
public class ClientFactoryBeanUtils {

    private final static Class CLIENT_FACTORY_BEAN_CLASS = ClientFactoryBean.class;

    public static void setupClientFactoryBean(AbstractBeanDefinition beanDefinition, String configurationId, String clientClassName) {
        //设置Calss 即代理工厂
        //注意，这里的BeanClass是生成Bean实例的工厂，不是Bean本身。
        // FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例，
        // 其返回的是该工厂Bean的getObject方法所返回的对象。
        beanDefinition.setBeanClass(CLIENT_FACTORY_BEAN_CLASS);
        //这里采用的是byType方式注入，类似的还有byName等
        //beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        if (configurationId != null && configurationId.length() > 0) {
            beanDefinition.getPropertyValues().add("easystreamConfiguration", new RuntimeBeanReference(configurationId));
        }
        //设置属性 即所对应的接口
        beanDefinition.getPropertyValues().add("interfaceClass", clientClassName);
    }

    public static String getBeanId(String id, Class beanClass, ParserContext parserContext) {
        return getBeanId(id, beanClass, parserContext.getRegistry());
    }


    public static String getBeanId(String id, Class beanClass, BeanDefinitionRegistry registry) {
        if (id == null || id.length() == 0) {
            String generatedBeanName = beanClass.getName();
            id = generatedBeanName;
            int counter = 2;
            while (registry.containsBeanDefinition(id)) {
                id = generatedBeanName + (counter++);
            }
        }
        return id;
    }
}
