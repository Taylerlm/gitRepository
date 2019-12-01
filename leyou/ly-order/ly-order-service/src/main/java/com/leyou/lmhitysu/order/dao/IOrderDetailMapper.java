package com.leyou.lmhitysu.order.dao;

import com.leyou.lmhitysu.order.model.Order;
import com.leyou.lmhitysu.order.model.OrderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IOrderDetailMapper {
    public void insert(OrderDetail orderDetail);
    public List<OrderDetail> findByProperty(Map map);
}
