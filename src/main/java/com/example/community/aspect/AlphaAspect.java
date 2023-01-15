package com.example.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/*@Component
@Aspect*/
public class AlphaAspect {//切面组件
    //切入点execution为 * 代表一切返回值类型，com.example.community.service是包名
    // .*.*是指包下的所有组件的所有方法，(..)所有的参数
    //这表达式很灵活
    @Pointcut("execution(* com.example.community.service.*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()")//连接点开始的地方记日志,针对pointcut这个连接点有效的
    public void before(){
        System.out.println("before");
    }

    @After("pointcut()")//连接点结束的地方记日志,针对pointcut这个连接点有效的
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()")//链接点返回之后记日志,针对pointcut这个连接点有效的
    public void afterReturning(){
        System.out.println("AfterReturning");
    }

    @AfterThrowing("pointcut()")//抛出异常之后记日志,针对pointcut这个连接点有效的
    public void afterThrowing(){
        System.out.println("AfterThrowing");
    }

    @Around("pointcut()")//既想在前面植入逻辑，又想在后面植入逻辑
    public Object around(ProceedingJoinPoint pJP) throws Throwable {
        //做点事情
        Object proceed = pJP.proceed();//业务组件的方法
        //做点事情
        return proceed;
    }


}
