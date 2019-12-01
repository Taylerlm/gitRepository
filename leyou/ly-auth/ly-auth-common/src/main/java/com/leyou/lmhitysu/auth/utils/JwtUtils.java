package com.leyou.lmhitysu.auth.utils;

import com.leyou.lmhitysu.auth.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtUtils {
    /**
     * 私钥加密获取token
     * @param userInfo 载荷中的数据
     * @param privateKey 私钥
     * @param expireMinutes　过期时间单位分钟
     * @return
     */
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey, int expireMinutes){
        return Jwts.builder().claim(JwtMDA.JWT_KEY_ID,userInfo.getId()).
                claim(JwtMDA.JWT_KEY_USERNAME,userInfo.getUsername()).
                setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate()).
                signWith(SignatureAlgorithm.RS256,privateKey).compact();
    }
    /**
     * 私钥加密token
     *
     * @param userInfo      载荷中的数据
     * @param privateKey    私钥字节数组
     * @param expireMinutes 过期时间，单位分钟
     * @return
     * @throws Exception
     */
    public static String generateToken(UserInfo userInfo, byte[] privateKey, int expireMinutes) throws Exception {
        return Jwts.builder()
                .claim(JwtMDA.JWT_KEY_ID, userInfo.getId())
                .claim(JwtMDA.JWT_KEY_USERNAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, RsaUtil.getPrivateKey(privateKey))
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtil.getPublicKey(publicKey))
                .parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static UserInfo getInfoFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                Long.valueOf(body.get(JwtMDA.JWT_KEY_ID).toString()),
                body.get(JwtMDA.JWT_KEY_USERNAME).toString()
        );
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static UserInfo getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                Long.valueOf(body.get(JwtMDA.JWT_KEY_ID).toString()),
                body.get(JwtMDA.JWT_KEY_USERNAME).toString()
        );
    }
}
