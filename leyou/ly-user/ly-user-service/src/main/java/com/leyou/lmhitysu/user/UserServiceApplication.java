package com.leyou.lmhitysu.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.leyou.lmhitysu.user.**.dao")
public class UserServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(UserServiceApplication.class,args);
    }

}
