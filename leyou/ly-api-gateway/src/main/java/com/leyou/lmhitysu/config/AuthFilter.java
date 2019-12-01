package com.leyou.lmhitysu.config;

import com.leyou.lmhitysu.auth.utils.JwtUtils;
import com.leyou.lmhitysu.common.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class,FilterPorperty.class})
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterPorperty filterPorperty;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //获取路径
        String requestURI = request.getRequestURI();
        //路径是不是在白名单里，在就放行
        //返回为true就不应该过滤  return false 就不过滤了
        return !isAllowPath(requestURI);
    }
    private Boolean isAllowPath(String requestURI){
        List<String> allowPaths = filterPorperty.getAllowPaths();
        Boolean flag = false;
        for(String path : allowPaths){
            if(requestURI.startsWith(path)){
                flag = true;
                break;
            }
        }
        return flag;
    }
    @Override
    public Object run() throws ZuulException {
        //校验token
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //获取cookie
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        //从token中获取用户信息，获取到就成功了
        try{
            //得到就放行
            JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
        }catch (Exception e){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }


        return null;
    }
}
