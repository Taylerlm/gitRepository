package com.leyou.lmhitysu.upload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:upload/upload.properties")
public class UploadFieConfiguration {

    @Value("${fastdfs.url}")
    public  String fastdfsUrl;
    public String getFastdfsUrl() {
        return fastdfsUrl;
    }

    public void setFastdfsUrl(String fastdfsUrl) {
        this.fastdfsUrl = fastdfsUrl;
    }

}
