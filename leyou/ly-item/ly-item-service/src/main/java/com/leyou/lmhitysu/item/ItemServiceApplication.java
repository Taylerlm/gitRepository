package com.leyou.lmhitysu.item;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.leyou.lmhitysu.item.**.dao")
public class ItemServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(ItemServiceApplication.class,args);
    }

}
