package com.leyou.lmhitysu.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CodecUtil {
    public static String passwordBcryptEncode(String username,String password){

        return new BCryptPasswordEncoder().encode(username + password);
    }

    public static Boolean passwordConfirm(String rawPassword,String encodePassword){
        return new BCryptPasswordEncoder().matches(rawPassword,encodePassword);
    }
}
