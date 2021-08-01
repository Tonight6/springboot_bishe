package com.springboot.bishe.common;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author hlt
 * @Date 2019/11/27 14:49
 */


/**
 *
        全局异常处理
        全局数据绑定 : 在该类定义数据完成后，在任何一个Controller 的接口中，都可以获取到这里定义的数据
        全局数据预处理

 */
@ControllerAdvice
public class GloableExceptionAop {
    /**
     * shiro异常
     * */
    @ResponseBody
    @ExceptionHandler(value = UnauthorizedException.class)//处理访问方法时权限不足问题
    public DataGridView defaultErrorHandler() {
        System.out.println("AuthorizationException异常");
        return DataGridView
                .fail("权限不足")
                .add("link", "/error/404");
    }
    //@ExceptionHandler 注解用来指明异常的处理类型，即如果这里指定为 NullpointerException
    // ，则数组越界异常就不会进到这个方法中来。


    /**
     * runtime异常
     * */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String runtimeException(){
        return "出现runtime异常了，这里在捕获全局异常，相当于手写AOP捕获异常。";
    }
}
