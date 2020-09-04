package com.easystream.boot.annotation;
/**
 * Created by soft001 on 2020/8/30.
 */

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName EasystreamScan
 * @Description: 接口入口扫描器
 * @Author soft001
 * @Date 2020/8/30
 * @Version V1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EasystreamScannerRegister.class})
public @interface EasystreamScan {
    String[] value() default {};

    String configuration() default "";

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
}
