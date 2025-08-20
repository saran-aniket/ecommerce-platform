package com.learn.usermicroservice.exceptions.handlers;

import com.learn.usermicroservice.dtos.GenericResponseDto;
import com.learn.usermicroservice.dtos.ResponseStatus;
import com.learn.usermicroservice.dtos.UserDto;
import com.learn.usermicroservice.exceptions.*;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends Exception {


    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleDuplicateEmailException(DuplicateEmailException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE,
                exception.getMessage(), null));
    }

    @ExceptionHandler(UserRoleDoesNotExistException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleUserRoleDoesNotExistException(UserRoleDoesNotExistException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleInvalidCredentialException(InvalidCredentialException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleInvalidTokenException(InvalidTokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleAuthenticationException(AuthenticationCredentialsNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleOtherExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleUnauthorizedExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<GenericResponseDto<UserDto>> handleJwtExceptions(JwtException exception) {
        log.error("Jwt Exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleFeignExceptions(CustomFeignException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(Integer.parseInt(exception.getStatus()))).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleSellerNotFoundException(SellerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }
}
