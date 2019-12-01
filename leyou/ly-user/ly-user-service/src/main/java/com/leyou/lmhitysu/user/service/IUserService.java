package com.leyou.lmhitysu.user.service;

import com.leyou.lmhitysu.user.model.User;

public interface IUserService {
    public Boolean checkUserData(String data,Integer type);
    public Boolean sendVertifyCode(String phone);
    public Boolean register(User user, String code);
    public User query(String username,String password);
}
