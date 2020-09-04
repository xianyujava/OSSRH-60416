package com.easystream.boot;

import com.easystream.boot.properties.StreamConfigurationProperties;
import com.easystream.spring.selector.EasystreamSelectImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ClassName EasystreamAutoConfiguration
 * @Description: TODO
 * @Author soft001
 * @Date 2020/8/30
 **/
@Configuration
//@ComponentScan("com.easystream.boot.properties")
@EnableConfigurationProperties({StreamConfigurationProperties.class})
@ComponentScan("com.easystream.spring.service") // 指定路径
@Import(EasystreamSelectImport.class)
public class EasystreamAutoConfiguration {
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    public EasystreamBeanRegister easystreamBeanRegister(StreamConfigurationProperties streamConfigurationProperties) {
        EasystreamBeanRegister easystreamBeanRegister = new EasystreamBeanRegister(applicationContext, streamConfigurationProperties);
        easystreamBeanRegister.registerEasystreamConfiguration(streamConfigurationProperties);
        easystreamBeanRegister.registerScanner(streamConfigurationProperties);
        return easystreamBeanRegister;
    }

}
