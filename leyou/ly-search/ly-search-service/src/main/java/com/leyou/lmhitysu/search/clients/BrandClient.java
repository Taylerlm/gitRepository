package com.leyou.lmhitysu.search.clients;

import com.lmhitysu.leyou.item.feignClient.IBrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ly-item-service")
public interface BrandClient extends IBrandApi {
}
