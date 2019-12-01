package com.leyou.lmhitysu.order.controller;

import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.order.model.Order;
import com.leyou.lmhitysu.order.service.IOrderService;
import com.leyou.lmhitysu.order.utils.PayHelper;
import com.leyou.lmhitysu.order.utils.PayState;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "order")
@Api("订单服务接口")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private PayHelper payHelper;
    @PostMapping(value = "createOrder")
    @ApiOperation(value = "创建订单接口，返回订单编号",notes = "创建订单")
    @ApiImplicitParam(name = "order",required = true,value = "订单的json对象，包含订单条目和物流信息")
    public ResponseEntity<Long> createOrder(@RequestBody Order order){
        Long id = this.orderService.createOrder(order);
        if(id == null){
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(id);
    }
    @GetMapping(value = "{id}")
    @ApiOperation(value = "根据订单编号查询订单，返回订单对象",notes = "查询订单")
    @ApiImplicitParam(name = "id",required = true,value = "订单编号")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id){
        Order order = this.orderService.queryOrderById(id);
        if(order == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(order);
    }
    @GetMapping("list")
    @ApiOperation(value = "分页查询当前用户订单，并且可以根据订单状态过滤",notes = "分页查询当前用户订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页",defaultValue = "1"),
            @ApiImplicitParam(name = "rows",value = "每页大小",defaultValue = "5"),
            @ApiImplicitParam(name = "status",value = "订单状态：1未付款，" +
                    "2已付款未发货，" +
                    "3已发货未确认，" +
                    "4已确认未评价，" +
                    "5交易关闭，" +
                    "6交易成功，已评价",defaultValue = "1")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "订单的分页结果"),
            @ApiResponse(code = 404, message = "没有查询到结果"),
            @ApiResponse(code = 500,message = "服务器异常")})

    public ResponseEntity<PageResult<Order>> queryUserOrderList(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                                @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                                                @RequestParam(value = "status",required = false) Integer status){
        PageResult<Order> result = this.orderService.queryUserOrderList(page,rows,status);
        if (result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    @PutMapping(value = "{id}/{status}")
    @ApiOperation(value = "更新订单状态",notes = "更新订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "订单编号"),
            @ApiImplicitParam(name = "status",value = "订单状态：1未付款，" +
                    "2已付款未发货，" +
                    "3已发货未确认，" +
                    "4已确认未评价，" +
                    "5交易关闭，" +
                    "6交易成功，已评价",defaultValue = "1")
    })
    @ApiResponses({
            @ApiResponse(code = 204,message = "true:修改成功；false:修改状态失败"),
            @ApiResponse(code = 400,message = "请求参数有误"),
            @ApiResponse(code = 500,message = "服务器异常")})
    public ResponseEntity<Boolean> updateOrderStatusById(@PathVariable(value = "id") Long id,@PathVariable(value = "status") Integer status){
        Boolean result = this.orderService.updateOrderStatusById(id,status);
        if (result == null){
            //返回400
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //返回204
        return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "url/{id}")
    @ApiOperation(value = "生成微信扫描支付付款链接",notes = "生成付款链接")
    @ApiImplicitParam(name = "id",value = "订单编号")
    @ApiResponses({
            @ApiResponse(code = 200,message = "根据订单编号生成的微信支付地址"),
            @ApiResponse(code = 404,message = "生成链接失败"),
            @ApiResponse(code = 500,message = "服务器异常")
    })
    public ResponseEntity<String> generateUrl(@PathVariable(value = "id") Long id){
        String url = this.payHelper.createPayUrl(id);
        if (StringUtils.isNotBlank(url)){
            return ResponseEntity.ok(url);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping(value = "state/{id}")
    @ApiOperation(value = "查询扫码支付的付款状态",notes = "查询付款状态")
    @ApiImplicitParam(name = "id",value = "订单编号")
    @ApiResponses({
            @ApiResponse(code = 200, message = "0, 未查询到支付信息 1,支付成功 2,支付失败"),
            @ApiResponse(code = 500, message = "服务器异常"),
    })
    public ResponseEntity<Integer> queryPayState(@PathVariable("id") Long orderId) {
        PayState payState = this.payHelper.queryOrder(orderId);
        return ResponseEntity.ok(payState.getValue());
    }
}
