//package com.springboot.bishe.Aspect;
//
//import com.springboot.bishe.annotation.Syslog;
//import com.springboot.bishe.common.RedisUtil;
//import com.springboot.bishe.domain.LogMethd;
//import com.springboot.bishe.service.LogMethdService;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * ClassName: LogCacheAspect
// * Package: com.springboot.bishe.Aspect
// *
// * @Description:
// * @Date: 2021/6/15 10:25
// * @author: 浪漫
// */
//
//@Component
//@Aspect
//@EnableAspectJAutoProxy
//public class LogMethodCacheAspect {
//    private static  String logKey="log-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));;
//
//
//    @Autowired
//    private LogMethdService sysLogService;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//
//
//    /**
//     * 这里我们使用注解的形式
//     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
//     * 切点表达式:   execution(...)
//     */
//    @Pointcut("@annotation(com.springboot.bishe.annotation.Syslog)")
//    public void logPointCut() {}
//
//
//    /**
//     * 保存日志
//     * @param joinPoint
//     */
//    @Around("logPointCut()")
//    private void saveLog(ProceedingJoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Syslog sysLog = method.getAnnotation(Syslog.class);
//
//        //获取servlet请求对象---因为这不是控制器，这里不能注入HttpServletRequest，但springMVC本身提供ServletRequestAttributes可以拿到
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        LogMethd log = new LogMethd();
//        // URL
//        log.setActionUrl(request.getRequestURL().toString());
//        // ip
//        log.setIp(request.getRemoteAddr());
//        // 开始时间
//        // System.currentTimeMillis()
//        Long beginTime = Instant.now().toEpochMilli();
//        log.setStartTime(beginTime);
//        if(sysLog != null){
//            //注解上的描述
//            log.setDescription(sysLog.value());
//        }
//        //请求的方法
//        log.setActionMethod(joinPoint.getSignature().getName());
//        // 类名
//        log.setClassPath(joinPoint.getTarget().getClass().getName());
//        //请求的参数
//        Object[] args = joinPoint.getArgs();
//        try{
//            List<String> list = new ArrayList<String>();
//            for (Object o : args) {
//                list.add((String) o);
//            }
//            log.setParams(list.toString());
//            // 执行切面方法
//            joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        // 结束时间
//        Long endTime = Instant.now().toEpochMilli();
//        log.setFinishTime(endTime);
//        //消耗时间
//        log.setConsumingTime(endTime - beginTime);
//        sysLogService.save(log);
//
//        System.out.println(log.getConsumingTime()+"++++++++");
//
//        redisUtil.sSet(logKey, log);
//    }
//}
