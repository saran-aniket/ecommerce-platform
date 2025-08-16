package com.learn.productservice.exceptions;

import com.learn.productservice.dtos.GenericResponseDto;
import com.learn.productservice.dtos.ResponseStatus;
import io.jsonwebtoken.JwtException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> productNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.badRequest().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE,
                ex.getMessage(), null));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<GenericResponseDto<Void>> jwtException(JwtException ex) {
        return ResponseEntity.internalServerError().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE, ex.getMessage(), null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GenericResponseDto<Void>> authenticationException(AuthenticationException ex) {
        return ResponseEntity.internalServerError().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE, ex.getMessage(), null));
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<GenericResponseDto<Void>> feignException(CustomFeignException ex) {
        return ResponseEntity.internalServerError().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE, ex.getMessage(), null));
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> inventoryNotFoundException(InventoryNotFoundException ex) {
        return ResponseEntity.badRequest().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE,
                ex.getMessage(), null));
    }

    @ExceptionHandler(DuplicateProductFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> duplicateProductFoundException(DuplicateProductFoundException ex) {
        return ResponseEntity.badRequest().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE,
                ex.getMessage(), null));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> userNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE,
                ex.getMessage(), null));
    }
}
