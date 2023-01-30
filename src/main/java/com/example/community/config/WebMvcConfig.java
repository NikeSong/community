package com.example.community.config;

import com.example.community.controller.interceptor.AlphaInterceptor;
import com.example.community.controller.interceptor.LoginRequiredInterceptor;
import com.example.community.controller.interceptor.LoginTicketInterceptor;
import com.example.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Autowired
    private MessageInterceptor messageInterceptor;

    //注册拦截器，定义好的拦截器必须在这里注册才是有效的
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(alphaInterceptor);//拦截一切请求

        //因为访问静态资源使用的是localHost:8080/community/css/...
        // /**表示static目录下所有文件夹 *.css表示所有目录下的所有css文件
        registry.addInterceptor(alphaInterceptor).excludePathPatterns("/**/*.css","/**/*.js", //使访问静态资源的请求都不拦截
                "/**/*.png","/**/*.jpeg","/**/*.jpg").addPathPatterns("/register","/login");//指定拦截某些内容,如注册
        //只能设置一条

        registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css","/**/*.js",
                "/**/*.png","/**/*.jpeg","/**/*.jpg");//使访问静态资源的请求都不拦截
        registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/*.css","/**/*.js",
                "/**/*.png","/**/*.jpeg","/**/*.jpg");//使访问静态资源的请求都不拦截
        registry.addInterceptor(messageInterceptor).excludePathPatterns("/**/*.css","/**/*.js",
                "/**/*.png","/**/*.jpeg","/**/*.jpg");//使访问静态资源的请求都不拦截
    }
}
