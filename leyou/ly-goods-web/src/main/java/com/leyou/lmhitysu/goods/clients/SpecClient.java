package com.leyou.lmhitysu.goods.clients;

import com.lmhitysu.leyou.item.feignClient.ISpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ly-item-service")
public interface SpecClient extends ISpecApi {
}
