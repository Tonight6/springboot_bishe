package com.springboot.bishe.Listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * ClassName: ContextListener
 * Package: com.springboot.bishe.Listener
 *
 * @Description:
 * @Date: 2021/5/6 9:30
 * @author: 浪漫
 */
@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("全局监听器启动=============");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("全局监听器销毁");
    }
}
