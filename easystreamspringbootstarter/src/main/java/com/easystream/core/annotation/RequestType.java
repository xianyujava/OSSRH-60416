package com.easystream.core.annotation;

import java.lang.annotation.*;

/**
 * @ClassName RequestType
 * @Description: TODO
 * @Author soft001
 * @Date 2020/9/1
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestType {
    String value() default "";
}
