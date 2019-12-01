package com.leyou.lmhitysu.auth.controller;

import com.leyou.lmhitysu.auth.config.JwtProperties;
import com.leyou.lmhitysu.auth.entity.UserInfo;
import com.leyou.lmhitysu.auth.service.IAuthService;
import com.leyou.lmhitysu.auth.utils.JwtUtils;
import com.leyou.lmhitysu.common.Exception.LyException;
import com.leyou.lmhitysu.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private JwtProperties prop;
    @Autowired
    private IAuthService authService;

    /**
     * 授权接口
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "accredit")
    public ResponseEntity<Void> authentication(@RequestParam(value = "username") String username,
                                               @RequestParam(value = "password") String password,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception{
        //１调用用户中心接口查询有没有用户
        String token = authService.authentication(username,password);
        if(StringUtils.isBlank(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //有用户将用户信息作为载荷，签名使用非对称加密生成ｔｏｋｅｎ放入ｃｏｏｋｉｅ中
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getCookieMaxAge(),true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 校验登录状态，校验过重新更新token
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue(value = "LY_TOKEN") String token,HttpServletRequest request,HttpServletResponse response) {
        //从token中解析出放入token中的载荷信息
        try{
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            //解析成功，就刷新ｔｏｋｅｎ
            token = JwtUtils.generateToken(userInfo,prop.getPrivateKey(),prop.getExpire());
            //将新生成的token在放回到cookie中去
            CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getCookieMaxAge(),true);
            return ResponseEntity.ok(userInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
