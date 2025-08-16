package com.learn.productservice.client;

import com.learn.productservice.configs.CustomFeignClientConfiguration;
import com.learn.productservice.dtos.GenericResponseDto;
import com.learn.productservice.dtos.GetUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://user-service:4002", path = "/api/v1/user", configuration = CustomFeignClientConfiguration.class)
public interface UserServiceClient {
    @GetMapping("/profile")
    GenericResponseDto<GetUserResponseDto> getUserProfileById(@RequestParam("roleType") String roleType,
                                                              @RequestParam("userId") String userId);
}
