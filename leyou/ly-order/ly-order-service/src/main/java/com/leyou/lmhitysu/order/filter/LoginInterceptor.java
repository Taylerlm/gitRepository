package com.leyou.lmhitysu.order.filter;

import com.leyou.lmhitysu.auth.entity.UserInfo;
import com.leyou.lmhitysu.auth.utils.JwtUtils;
import com.leyou.lmhitysu.common.utils.CookieUtils;
import com.leyou.lmhitysu.order.properties.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private JwtProperties jwtProperties;
    // 定义一个线程域，存放登录用户
    //这里我们使用了ThreadLocal来存储查询到的用户信息，线程内共享，因此请求到达Controller后可以共享User
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();
    public LoginInterceptor(JwtProperties properties){
        this.jwtProperties = properties;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查询token
        String token = CookieUtils.getCookieValue(request,"LY_TOKEN");
        if(StringUtils.isBlank(token)){
            //不存在ｔｏｋｅｎ返回false
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try{
            //解析出来了ｕｓｅｒ信息 放行
            UserInfo userInfo = JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
            // 放入线程域
            tl.set(userInfo);
            return true;
        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

    }
    @Override
    public void  afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        tl.remove();
    }
    public static UserInfo getLoginUser(){
        return tl.get();
    }
}
