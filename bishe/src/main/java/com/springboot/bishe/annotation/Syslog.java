package com.springboot.bishe.annotation;

import java.lang.annotation.*;

/**
 * ClassName: Syslog
 * Package: com.springboot.annotation
 *
 * @Description:
 *      自定义系统日志注解
 * @Date: 2021/5/23 15:48
 * @author: 浪漫
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Syslog {
    String value() default "";
}
