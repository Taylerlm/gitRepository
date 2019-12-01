package com.leyou.lmhitysu.order.config;

import com.leyou.lmhitysu.order.filter.LoginInterceptor;
import com.leyou.lmhitysu.order.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableWebMvc
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtProperties properties;
    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor(properties);
    }
    /**
     * 配置拦截路径是所有的都拦截
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/swagger-ui.html");
        excludePath.add("/swagger-resources/**");
        excludePath.add("/webjars/**");
        excludePath.add("/v2/**");
        excludePath.add("/configuration/**");
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);
    }
}
