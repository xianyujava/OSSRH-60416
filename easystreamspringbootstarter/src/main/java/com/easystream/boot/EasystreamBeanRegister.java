package com.easystream.boot;

import com.easystream.boot.annotation.EasystreamScannerRegister;
import com.easystream.boot.properties.StreamConfigurationProperties;
import com.easystream.core.EasystreamConfiguration;
import com.easystream.core.stream.TimerUtil;
import com.easystream.core.utils.StringUtils;
import com.easystream.spring.scanner.ClassPathClientScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

/**
 * @ClassName EasystreamBeanRegister
 * @Description: TODO
 * @Author soft001
 * @Date 2020/8/30
 **/
public class EasystreamBeanRegister implements ResourceLoaderAware, BeanPostProcessor {
    private final ConfigurableApplicationContext applicationContext;

    private ResourceLoader resourceLoader;

    private StreamConfigurationProperties streamConfigurationProperties;

    public EasystreamBeanRegister(ConfigurableApplicationContext applicationContext, StreamConfigurationProperties streamConfigurationProperties) {
        this.applicationContext = applicationContext;
        this.streamConfigurationProperties = streamConfigurationProperties;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public EasystreamConfiguration registerEasystreamConfiguration(StreamConfigurationProperties streamConfigurationProperties) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(EasystreamConfiguration.class);
        String id = streamConfigurationProperties.getBeanId();
        if (StringUtils.isBlank(id)) {
            id = "easystreamConfiguration";
        }
        beanDefinitionBuilder
                .addPropertyValue("keepalive", streamConfigurationProperties.getKeepalive())
                .addPropertyValue("push_host", streamConfigurationProperties.getPush_host())
                .addPropertyValue("host_extra", streamConfigurationProperties.getHost_extra())
                .addPropertyValue("push_port", streamConfigurationProperties.getPush_port())
                .addPropertyValue("main_code", streamConfigurationProperties.getMain_code())
                .addPropertyValue("sub_code", streamConfigurationProperties.getSub_code())
                .addPropertyValue("rtsptype", streamConfigurationProperties.getRtsptype())
                .addPropertyValue("access_port", streamConfigurationProperties.getAccess_port())
                .setLazyInit(false)
                .setFactoryMethod("configuration");

        // BeanDefinition interceptorFactoryBeanDefinition = registerInterceptorFactoryBean();
        // beanDefinitionBuilder.addPropertyValue("interceptorFactory", interceptorFactoryBeanDefinition);


        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        //beanDefinition.getPropertyValues().addPropertyValue("sslKeyStores", sslKeystoreMap);

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(id, beanDefinition);

        EasystreamConfiguration configuration = applicationContext.getBean(id, EasystreamConfiguration.class);
        //定时器删除不在预览得视频流
        new TimerUtil(configuration).run();
        return configuration;
    }

    public ClassPathClientScanner registerScanner(StreamConfigurationProperties forestConfigurationProperties) {
        List<String> basePackages = EasystreamScannerRegister.basePackages;
        String configurationId = EasystreamScannerRegister.configurationId;
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();

        ClassPathClientScanner scanner = new ClassPathClientScanner(configurationId, registry);

        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
//        scanner.registerFilters();
        if (basePackages == null || basePackages.size() == 0) {
            return scanner;
        }
        scanner.doScan(org.springframework.util.StringUtils.toStringArray(basePackages));
        return scanner;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        registerEasystreamConfiguration(streamConfigurationProperties);
        registerScanner(streamConfigurationProperties);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
