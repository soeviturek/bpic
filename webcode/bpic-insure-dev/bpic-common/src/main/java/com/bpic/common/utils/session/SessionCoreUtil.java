package com.bpic.common.utils.session;


import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description: 获取session
 * @author: guiyang
 * @date: 2018/1/29 15:43
 */
@Component
public class SessionCoreUtil {

    private static HttpServletRequest request = null;

    private static HttpSession session = null;

    //获取当前上下文对应的request请求
    public static HttpServletRequest getRequest(){
        if (request == null){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
           request = attributes.getRequest();
        }
        return request;
    }

    //获取当前上下文对应的request请求
    public static HttpServletRequest getHttpRequest(){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
           request = attributes.getRequest();
        return request;
    }
    

    //获取当前上下文对应的session请求
    public static HttpSession getSession(){
        if (session == null){
            session = getRequest().getSession();
        }
        return session;
    }

    //给session赋值
    public static void setAttribute(String key,Object value){
        getSession().setAttribute(key,value);
    }

    //session取值
    public static Object getAttribute(String key){
        return getSession().getAttribute(key);
    }
    
    //header取值
    public static Object getHeader(String key){
        return getHttpRequest().getHeader(key);
    }


}