package com.leyou.lmhitysu.user.service;

import com.leyou.lmhitysu.common.utils.CodecUtil;
import com.leyou.lmhitysu.common.utils.NumberUtils;
import com.leyou.lmhitysu.user.dao.IUserMapper;
import com.leyou.lmhitysu.user.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service(value = "userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "user:phone:code";
    @Override
    public Boolean checkUserData(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;
        }
        Integer integer = this.userMapper.selectCount(user);
        if(integer.equals(0)){
            return true;
        }
        return false;
    }

    @Override
    public Boolean sendVertifyCode(String phone) {
        //生成验证吗
        String code = NumberUtils.generateCode(6);
        try{
            Map<String,String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            //发送消息发送短信
            this.amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
            //将验证码保存到ｒｅｄｉｓ
            this.redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean register(User user, String code) {
        //校验用户数据
        //校验手机短信验证码
        String codeRedis = this.redisTemplate.opsForValue().get(KEY_PREFIX+user.getPhone());
        if(code == null || !code.equals(codeRedis)){
            return null;
        }
        //生成盐ｓａｌｔ

        user.setCreated(new Date());
        // BCryptPasswordEncoder 使用这个加密算法的盐会自动生成．　数据库中也不保存salt了
        //加密算法的详情请看博客
        user.setPassword(CodecUtil.passwordBcryptEncode(user.getUsername(),user.getPassword()));
        //写入数据库
        boolean boo = this.userMapper.create(user) == 1;
        if(boo){
            try{
                //删除校验码
                this.redisTemplate.delete(KEY_PREFIX+user.getPhone());
            }catch (Exception e){

            }
        }
        return boo;
    }

    @Override
    public User query(String username, String password) {
        //只根据用户名去查询
        User record = new User();
        record.setUsername(username);
        //查到了就说明存在了
        List<User> users = this.userMapper.findByProperty(record);
        //查不到就说明没有
        User user = null;
        if(users != null && users.size() == 1){
            //查到后校验密码
            user = users.get(0);
        }
        if(user != null){
            //3. 校验密码
            boolean result = CodecUtil.passwordConfirm(username + password,user.getPassword());
            if (!result){
                return null;
            }
        }
        return user;
    }
}
