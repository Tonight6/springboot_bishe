package com.springboot.bishe.common;

import com.springboot.bishe.domain.User;
import com.springboot.bishe.vo.ActiverUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 登陆拦截器：未登录用户不能访问系统界面
 */
public class LoginHandlerInterceptor implements HandlerInterceptor{


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()){
            //已登陆，放行请求
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            System.out.println(activerUser.getUser().getUsername()+"用户已经登陆，直接进入index页面");
            UsernamePasswordToken token = new UsernamePasswordToken(activerUser.getUser().getUsername(),activerUser.getUser().getPassword());
            subject.login(token);
            request.getRequestDispatcher("/index.html").forward(request,response);
            return true;
        }else {
            //未登陆，返回登陆页面
            request.setAttribute("msg","没有权限请先登陆");
            response.sendRedirect("/login");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
