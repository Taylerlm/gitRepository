package com.leyou.lmhitysu.order.properties;

import com.leyou.lmhitysu.auth.utils.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String pubKeyPath;
    private String cookieName;

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    private PublicKey publicKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);
    @PostConstruct
    public void init(){
        try {
            this.publicKey = RsaUtil.getPublicKey(pubKeyPath);
        }catch (Exception e){
            logger.error("初始化公钥失败！", e);
            throw new RuntimeException();
        }

    }
    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
