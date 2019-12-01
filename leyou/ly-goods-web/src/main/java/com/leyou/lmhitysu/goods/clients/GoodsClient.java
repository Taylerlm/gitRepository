package com.leyou.lmhitysu.goods.clients;

import com.lmhitysu.leyou.item.feignClient.IGoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ly-item-service")
public interface GoodsClient extends IGoodsApi {
}
