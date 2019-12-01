package com.leyou.lmhitysu.order.service;

import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.order.model.Order;

public interface IOrderService {
    public Long createOrder(Order order);

    Order queryOrderById(Long id);

    PageResult<Order> queryUserOrderList(Integer page, Integer rows, Integer status);

    Boolean updateOrderStatusById(Long id, Integer status);
}
