package com.springboot.bishe.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.Data;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: shiroConfig
 * Package: com.springboot.bishe.common.shiro
 *
 * @Description:
 * @Date: 2021/3/31 14:32
 * @author: 浪漫
 */


@Configuration
@ConfigurationProperties("shiro")
@ConditionalOnClass(value = { SecurityManager.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Data
public class shiroConfig {


    // 默认的登陆页面
    private String loginUrl;


    // anon 游客能访问的url
    private String[] anonUrls;

    // 登出url
    private String logOutUrl;

    // authc 授权后的url
    private String[] authcUlrs;


  //  private String[]  userUrls;

    //记住我
    @Bean("rememberMeManager")
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //这个地方有点坑，不是所有的base64编码都可以用，长度过大过小都不行，没搞明白，官网给出的要么0x开头十六进制，要么base64
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    //cookie管理
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        // 秒为单位
        cookie.setMaxAge(60 * 60);
        return cookie;
    }

    // 配置SecurityManager
    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("UserRealm") Realm myRealm,
                                                     @Qualifier("rememberMeManager") RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置一个Realm，这个Realm是最终用于完成我们的认证号和授权操作的具体对象
        securityManager.setRealm(myRealm);

        //设置RememberMeManager 实现记住我功能
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    //配置一个自定义的Realm的bean，最终将使用这个bean返回的对象来完成我们的认证和授权
    @Bean("UserRealm")
    public Realm myRealm() {
        UserRealm realm = new UserRealm();
        return realm;
    }


    /**
     * 配置shiro的过滤器
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        // 设置未登陆的时要跳转的页面
        factoryBean.setLoginUrl(loginUrl);
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 设置放行的路径
        if (anonUrls != null && anonUrls.length > 0) {
            for (String anon : anonUrls) {
                filterChainDefinitionMap.put(anon, "anon");
            }
        }
        // 设置登出的路径
        if (null != logOutUrl) {
            filterChainDefinitionMap.put(logOutUrl, "logout");
        }
        // 设置拦截的路径
        if (authcUlrs != null && authcUlrs.length > 0) {
            for (String authc : authcUlrs) {
                filterChainDefinitionMap.put(authc, "authc");
            }
        }
        // 设置记住我(user)的路径
//        if (userUrls != null && authcUlrs.length > 0) {
//            for (String user : userUrls) {
//                filterChainDefinitionMap.put(user, "user");
//            }
//
//        }
        Map<String, Filter> filters = new HashMap<>();
//		filters.put("authc", new ShiroLoginFilter());
        //配置过滤器
        factoryBean.setFilters(filters);
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }


    /* 加入注解的使用，不加入这个注解不生效--开始 */
    /**
     *
     * @param securityManager
     * @return
     */

    /**
     * 开启AOP的注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /*   注意：启动注解的权限控制以后需要删除在Shiro配置类中的权限拦截的配置规则
        map.put("/admin/test","authc,perms[admin:add]");
        map.put("/admin/**","authc,roles[admin]");
        map.put("/user/**","authc,roles[user]");*/

    /**
     * 开启Shiro的注解例如（  @RequiresRoles @RequiresUser @RequiresPermissions）
     * * 需要借助SpringAOP来扫描这些注解
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    /* 加入注解的使用，不加入这个注解不生效--结束 */

    // 开启thymeleaf的shiro标签
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


}

