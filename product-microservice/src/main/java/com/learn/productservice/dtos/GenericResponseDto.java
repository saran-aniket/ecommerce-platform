package com.learn.productservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Data
@Getter
public class GenericResponseDto<T> {
    private ResponseStatus status;
    private String message;
    private T data;

    public static <T> GenericResponseDto<T> GenericResponseDtoFrom(ResponseStatus status, String message, T data) {
        return GenericResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
