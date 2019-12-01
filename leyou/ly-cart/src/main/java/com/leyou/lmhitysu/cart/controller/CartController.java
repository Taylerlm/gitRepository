package com.leyou.lmhitysu.cart.controller;

import com.leyou.lmhitysu.cart.model.Cart;
import com.leyou.lmhitysu.cart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private ICartService cartService;

    @PostMapping(value = "/addCart")
    public ResponseEntity<Void> addCartToRedis(@RequestBody Cart cart) throws Exception{
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/queryCarts")
    public ResponseEntity<List<Cart>> queryCarts() throws Exception{
        List<Cart> carts = this.cartService.queryCarts();
        if(carts == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(carts);
    }
    @PutMapping(value = "/updateNum")
    public ResponseEntity<Void> updateNum(@RequestParam(value = "skuId") Long skuId,
                                          @RequestParam(value = "num") Integer num) throws Exception{
        this.cartService.updateNum(skuId,num);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(value = "/deleteSkuId")
    public ResponseEntity<Void> deleteSkuId(@RequestParam(value = "skuId") Long skuId){
        this.cartService.deleteSkuId(skuId);
        return ResponseEntity.ok().build();
    }
}
