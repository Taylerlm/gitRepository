package com.leyou.lmhitysu.auth.service;

import com.leyou.lmhitysu.auth.config.JwtProperties;
import com.leyou.lmhitysu.auth.entity.UserInfo;
import com.leyou.lmhitysu.auth.feignClients.UserFeignClient;
import com.leyou.lmhitysu.auth.utils.JwtUtils;
import com.leyou.lmhitysu.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public String authentication(String username, String password) throws Exception{
        //调用用户中心接口

            User user = null;
            ResponseEntity<User> userEntity = userFeignClient.query(username, password);
            if(userEntity.hasBody()){
                user = userEntity.getBody();
            }
            if(user == null){
                return null;
            }
            //根据用户信息生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return token;
    }
}
