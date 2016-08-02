package com.toutiao.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * Created by admin on 16-7-2.
 */

//调用所有页面之前和之后走这个切面，结果显示在控制台中
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LogAspect.class );
    @Before("execution(* com.toutiao.controller.*Controller.*(..))")

    //joinPoint 是切点
    public void beforMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for (Object arg:joinPoint.getArgs()){
            sb.append("arg:"+arg.toString()+"|");
        }
        logger.info("before:time",new Date());
        logger.info("before method:"+sb.toString());
    }

    @After("execution(* com.toutiao.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("after method:");
    }
}
