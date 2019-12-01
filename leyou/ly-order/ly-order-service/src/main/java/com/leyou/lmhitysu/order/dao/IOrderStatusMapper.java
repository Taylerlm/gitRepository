package com.leyou.lmhitysu.order.dao;

import com.leyou.lmhitysu.order.model.Order;
import com.leyou.lmhitysu.order.model.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IOrderStatusMapper {
    public void insert(OrderStatus order);
    public List<OrderStatus> findByProperty(Map map);
    public int update(OrderStatus orderStatus);
}
