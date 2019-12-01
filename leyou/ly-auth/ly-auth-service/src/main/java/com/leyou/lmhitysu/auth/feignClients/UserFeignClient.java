package com.leyou.lmhitysu.auth.feignClients;

import com.leyou.lmhitysu.user.clients.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ly-user-service")
public interface UserFeignClient extends UserApi {
}
