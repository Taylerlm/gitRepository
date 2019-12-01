package com.leyou.lmhitysu.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UploadFileApplication {
    public static void main(String[] args){
        SpringApplication.run(UploadFileApplication.class,args);
    }
}
