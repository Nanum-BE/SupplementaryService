package com.nanum.supplementaryservice.client;

import com.nanum.supplementaryservice.client.vo.UserDto;
import com.nanum.supplementaryservice.client.vo.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping(value = "/api/v1/users/{userId}", produces = "application/json")
   UserResponse<UserDto> getUser(@PathVariable("userId") Long userId);

    @GetMapping(value = "/api/v1/users/particular", produces = "application/json")
    UserResponse<List<UserDto>> getUsersById(@RequestParam(value="param", required=false, defaultValue="")
    List<Long> params);


}
