package com.leyou.lmhitysu.order.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.lmhitysu.auth.entity.UserInfo;
import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.common.utils.IdWorker;
import com.leyou.lmhitysu.order.dao.IOrderDetailMapper;
import com.leyou.lmhitysu.order.dao.IOrderMapper;
import com.leyou.lmhitysu.order.dao.IOrderStatusMapper;
import com.leyou.lmhitysu.order.filter.LoginInterceptor;
import com.leyou.lmhitysu.order.model.Order;
import com.leyou.lmhitysu.order.model.OrderDetail;
import com.leyou.lmhitysu.order.model.OrderStatus;
import com.leyou.lmhitysu.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderMapper orderMapper;
    @Autowired
    private IOrderStatusMapper orderStatusMapper;
    @Autowired
    private IOrderDetailMapper orderDetailMapper;
    @Autowired
    private IdWorker idWorker;
    @Override
    public Long createOrder(Order order) {
        /**
         * 生成订单id
         * 获取登录用户的信息
         * 初始化订单数据：买家昵称、是否评论、创建时间、订单号、用户id
         * 保存订单数据
         * 初始化订单状态数据：订单id、订单创建时间、订单状态（初始状态：1，未付款）
         * 保存订单状态数据
         * 为订单详情中的数据添加订单号，因为一个订单下有多个订单项
         * 保存订单详情数据
         * 减库存
         * */
        long id = idWorker.nextId();
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //初始化订单数据
        order.setBuyerNick(loginUser.getUsername());
        order.setBuyerRate(false);
        order.setCreateTime(new Date());
        order.setOrderId(id);
        order.setUserId(loginUser.getId());
        //保存数据
        this.orderMapper.insert(order);
        //初始化订单状态数据
        //5.保存订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(id);
        orderStatus.setCreateTime(order.getCreateTime());
        //初始状态未未付款：1
        orderStatus.setStatus(1);
        this.orderStatusMapper.insert(orderStatus);
        //7.在订单详情中添加orderId
        order.getOrderDetails().forEach(orderDetail -> orderDetail.setOrderId(id));
        //8.保存订单详情，使用批量插入功能
        for(OrderDetail orderDetail : order.getOrderDetails()){
            this.orderDetailMapper.insert(orderDetail);
        }
        return id;
    }

    @Override
    public Order queryOrderById(Long id) {
        Map<String,Object> orderM =  new HashMap<>();
        orderM.put("orderId",id);
        List<Order> orders = this.orderMapper.findByProperty(orderM);
        //查询当前订单下的订单详情
        Order result = null;
        if(orders != null && orders.size() == 1){
            List<OrderDetail> orderDetails = this.orderDetailMapper.findByProperty(orderM);
            List<OrderStatus> orderStatus = this.orderStatusMapper.findByProperty(orderM);
            orders.get(0).setOrderDetails(orderDetails);
            if(orderStatus != null && orderStatus.size() == 1){
                orders.get(0).setStatus(orderStatus.get(0).getStatus());
            }
            result = orders.get(0);
        }

        return result;
    }

    @Override
    public PageResult<Order> queryUserOrderList(Integer page, Integer rows, Integer status) {
        PageHelper.startPage(page,rows);
        //获取登录用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        Map orderM = new HashMap();
        orderM.put("userId",loginUser.getId());
        orderM.put("status",status);
        Page<Order> pageInfo = (Page<Order>) this.orderMapper.queryOrderList(orderM);
        List<Order> result = pageInfo.getResult();
        if(result != null && result.size() >0 ){
            Map orderDM = new HashMap();
            for(Order order : result){
                orderDM.put("orderId",order.getOrderId());
                List<OrderDetail> byProperty = this.orderDetailMapper.findByProperty(orderDM);
                order.setOrderDetails(byProperty);
            }
        }
        return new PageResult<>(pageInfo.getTotal(),result);
    }

    @Override
    public Boolean updateOrderStatusById(Long id, Integer status) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(id);
        orderStatus.setStatus(status);
        //1.根据状态判断要修改的时间
        switch (status){
            case 2:
                //2.付款时间
                orderStatus.setPaymentTime(new Date());
                break;
            case 3:
                //3.发货时间
                orderStatus.setConsignTime(new Date());
                break;
            case 4:
                //4.确认收货，订单结束
                orderStatus.setEndTime(new Date());
                break;
            case 5:
                //5.交易失败，订单关闭
                orderStatus.setCloseTime(new Date());
                break;
            case 6:
                //6.评价时间
                orderStatus.setCommentTime(new Date());
                break;

            default:
                return null;
        }
        int count = this.orderStatusMapper.update(orderStatus);
        return count == 1;
    }
}
