package com.easystream.spring.scanner;

import com.easystream.spring.utils.ClientFactoryBeanUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * @ClassName ClassPathClientScanner
 * @Description: TODO
 * @Author soft001
 * @Date 2020/8/31
 **/
public class ClassPathClientScanner extends ClassPathBeanDefinitionScanner {
    private final String configurationId;
    private boolean allInterfaces = true;
    public ClassPathClientScanner(String configurationId, BeanDefinitionRegistry registry) {
        super(registry,false);
        this.configurationId = configurationId;
        registerFilters();
    }


     /**
      * @MethodName:
      * @Description: 给扫描出来的接口添加上代理对象
      * @Param:
      * @Return:
      * @Author: soft001
      * @Date: 2020/9/1
     **/
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("[easystream] Creating easystream Client Bean with name '" + holder.getBeanName()
                        + "' and Proxy of '" + definition.getBeanClassName() + "' client interface");
            }
            //拿到接口的全路径名称
            String beanClassName = definition.getBeanClassName();
            //设置动态代理工厂
            ClientFactoryBeanUtils.setupClientFactoryBean(definition, configurationId, beanClassName);
            logger.info("[easystream] Created easystream Client Bean with name '" + holder.getBeanName()
                    + "' and Proxy of '" + beanClassName + "' client interface");

        }
    }

    /**
     * 注册过滤器  //添加过滤条件
     */
    public void registerFilters() {
        if (allInterfaces) {
            // include all interfaces
            addIncludeFilter(new TypeFilter() {
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
        //添加过滤条件，这里是只添加了@EasystreamService的注解才会被扫描到
        //addIncludeFilter(new AnnotationTypeFilter(EasystreamServiceScan.class));
    }
    /**
     * 重写扫描逻辑
     * @param basePackages
     * @return
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //调用spring的扫描
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("[easystream] No easystream client is found in package '" + Arrays.toString(basePackages) + "'.");
        }
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
