package com.leyou.lmhitysu.cart.service;

import com.leyou.lmhitysu.cart.model.Cart;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

public interface ICartService {
    public void addCart(Cart cart) throws Exception;

    public List<Cart> queryCarts() throws Exception;

    public void updateNum(Long skuId, Integer num) throws Exception;

    public void deleteSkuId(Long skuId);
}