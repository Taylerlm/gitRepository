package com.leyou.lmhitysu.user.dao;

import com.leyou.lmhitysu.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserMapper {
    public Integer selectCount(User user);
    public int create(User user);
    public List<User> findByProperty(User user);
}
