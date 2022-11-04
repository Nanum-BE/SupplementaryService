package com.nanum.supplementaryservice.client.vo;

import lombok.Data;

@Data
public class UserResponse<T> {
    private T result;
}
