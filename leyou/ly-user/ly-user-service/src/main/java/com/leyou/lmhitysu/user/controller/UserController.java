package com.leyou.lmhitysu.user.controller;

import com.leyou.lmhitysu.user.model.User;
import com.leyou.lmhitysu.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 校验用户注册时是否已经被注册了
     * @return
     */
    @GetMapping(value = "check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable(value = "data") String data,
                                                 @PathVariable(value = "type") Integer type){
        Boolean boo = this.userService.checkUserData(data,type);
        if(boo == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(boo);
    }
    /**
     * 发送手机验证码
     */
    @PostMapping(value="code")
    public ResponseEntity<Void> sendSmsCode(String phone){
        Boolean boo = this.userService.sendVertifyCode(phone);
        if(boo){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 注册功能
     */
    @PostMapping(value = "register")
    public ResponseEntity<Void> register(User user, @RequestParam(value = "code") String code){
        Boolean boo = this.userService.register(user,code);
        if(boo == null || !boo){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    /**
     * 根据用户名和密码查询用户
     */
    @GetMapping(value = "query")
    public ResponseEntity<User> query(@RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password){
        User user = this.userService.query(username,password);
        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }
}
