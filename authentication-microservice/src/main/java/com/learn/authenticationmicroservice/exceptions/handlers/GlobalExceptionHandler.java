package com.learn.authenticationmicroservice.exceptions.handlers;

import com.learn.authenticationmicroservice.dtos.GenericResponseDto;
import com.learn.authenticationmicroservice.dtos.ResponseStatus;
import com.learn.authenticationmicroservice.exceptions.*;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends Exception {


    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleDuplicateEmailException(DuplicateEmailException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.FAILURE,
                exception.getMessage(), null));
    }

    @ExceptionHandler(UserRoleDoesNotExistException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleUserRoleDoesNotExistException(UserRoleDoesNotExistException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleInvalidCredentialException(InvalidCredentialException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleInvalidTokenException(InvalidTokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleAuthenticationException(AuthenticationCredentialsNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponseDto<Void>> handleOtherExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleUnauthorizedExceptions(UnauthorizedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<GenericResponseDto<Void>> handleFeignExceptions(CustomFeignException exception) {
        return ResponseEntity.status(HttpStatus.valueOf(Integer.parseInt(exception.getStatus()))).body(GenericResponseDto.GenericResponseDtoFrom(
                ResponseStatus.FAILURE, exception.getMessage(), null
        ));
    }
}
