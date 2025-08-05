package com.learn.apigateway.exceptions.handlers;

import com.learn.apigateway.dtos.GenericResponseDto;
import com.learn.apigateway.dtos.ResponseStatus;
import com.learn.apigateway.exceptions.CustomFeignException;
import com.learn.apigateway.exceptions.GatewayAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GatewayAuthException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleException(GatewayAuthException ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE, ex.getMessage(), null));
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleFeignExceptions(CustomFeignException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(Integer.parseInt(exception.getStatus()))).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }
}
