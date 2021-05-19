package com.springboot.bishe.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * ClassName: mybatisPlus
 * Package: com.springboot.bishe.config
 *
 * @Description:
 * @Date: 2021/4/7 17:14
 * @author: 浪漫
 */
@Configuration
public class mybatisPlus {
    @Bean  //让Spring容器进行管理
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        return page;
    }

}
