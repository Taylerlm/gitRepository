package com.leyou.lmhitysu.goods.clients;

import com.lmhitysu.leyou.item.feignClient.ICategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ly-item-service")
public interface CategoryClients extends ICategoryApi {
}
