package com.leyou.lmhitysu.cart.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.lmhitysu.auth.entity.UserInfo;
import com.leyou.lmhitysu.cart.clients.GoodsClient;
import com.leyou.lmhitysu.cart.config.LoginInterceptor;
import com.leyou.lmhitysu.cart.model.Cart;
import com.lmhitysu.leyou.item.model.Sku;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements ICartService {

    static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodsClient goodsClient;
    private ObjectMapper mapper = new ObjectMapper();
    public static final String KEY_PREFIX = "LY:CART:UID:";
    @Override
    public void addCart(Cart cart) throws Exception{
        //添加到ｒｅｄｉｓ是先查当前用户的购物车信息是否存在
        //获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //查询是否存在当前用户的购物车信息
        String key = KEY_PREFIX+loginUser.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        //查询是否存在　这里有一个小问题要是当前客户的购物车信息在ｒｅｄｉｓ中不存在的话
        //返回的ｈａｓｈＯｐｓ是不是ｎｕｌｌ,从结果看来是不为ｎｕｌｌ
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean aBoolean = hashOps.hasKey(skuId.toString());
        if(aBoolean){
            //存在
            //更新当前商品的数量
            //查询获取到当前商品对应的信息
            String json = hashOps.get(skuId.toString()).toString();
            cart = mapper.readValue(json,new TypeReference<Cart>(){});
            cart.setNum(cart.getNum()+num);
        }else{
            //不存在要查询商品详情信息
            ResponseEntity<Sku> skuById = goodsClient.getSkuById(skuId);
            if(skuById.hasBody()){
                Sku sku = skuById.getBody();
                cart.setUserId(loginUser.getId());
                cart.setNum(num);
                cart.setTitle(sku.getTitle());
                cart.setOwnSpec(sku.getOwnSpec());
                cart.setPrice(sku.getPrice());
                cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            }

        }
        //然后映射成Ｃａｒｔ设置数量后保存到ｒｅｄｉｓ
        //然后在查用户对应的ｓｋｕｉｄ是不是存在
        // 将购物车数据写入redis
        hashOps.put(cart.getSkuId().toString(), mapper.writeValueAsString(cart));
    }

    @Override
    public List<Cart> queryCarts() throws Exception{
        List<Cart> carts = new ArrayList<>();
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //查询是否存在当前用户的购物车信息
        String key = KEY_PREFIX+loginUser.getId();
        if(!this.redisTemplate.hasKey(key)){
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> objects = hashOps.values();
        if(objects == null || objects.size() == 0){
            return null;
        }
        for(Object o : objects){
            carts.add(mapper.readValue(o.toString(), new TypeReference<Cart>() {
            }));

        }
        return carts;
    }

    @Override
    public void updateNum(Long skuId, Integer num) throws Exception{
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //查询是否存在当前用户的购物车信息
        String key = KEY_PREFIX+loginUser.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        String json = (String)hashOps.get(skuId.toString());
        Cart cart = mapper.readValue(json,new TypeReference<Cart>(){});
        cart.setNum(num);
        // 写入购物车
        hashOps.put(skuId.toString(), mapper.writeValueAsString(cart));
    }

    @Override
    public void deleteSkuId(Long skuId) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //查询是否存在当前用户的购物车信息
        String key = KEY_PREFIX+loginUser.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }
}
