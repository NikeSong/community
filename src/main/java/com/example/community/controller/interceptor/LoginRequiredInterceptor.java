package com.example.community.controller.interceptor;

import com.example.community.annotation.LoginRequired;
import com.example.community.util.HostHolder;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){//当前请求是一个方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);//获得此方法上这个类型的注解
            if(annotation != null && hostHolder.getUser()==null ){
                response.sendRedirect(request.getContextPath()+"/login");//返回登陆页面
                return false;//拒绝后续请求
            }
        }
        return true;
    }
}
