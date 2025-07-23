package com.learn.authenticationmicroservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class GenericResponseDto<T> {
    private ResponseStatus status;
    private String message;
    private T data;

    public static <T> GenericResponseDto<T> GenericResponseDtoFrom(ResponseStatus status, String message, T data){
        return GenericResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
