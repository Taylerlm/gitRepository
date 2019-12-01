package com.leyou.lmhitysu.order.dao;

import com.leyou.lmhitysu.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IOrderMapper {
    public void insert(Order order);
    public List<Order> findByProperty(Map map);
    public List<Order> queryOrderList(Map map);
}
