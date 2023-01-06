package com.example.community.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AlphaInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);

    /**
     * 下面三个方法中的Object handler存的是拦截的方法
     */

    //在Controller之前执行，返回false则取消请求，controller不处理请求，一般返回true
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandel:"+handler.toString());
        return true;
    }
    //在调用完controller之后执行，模板之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

        logger.debug("postHandel:"+handler.toString());
    }

    //模板引擎结束之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        logger.debug("afterCompletion:" + handler.toString());
    }
}
