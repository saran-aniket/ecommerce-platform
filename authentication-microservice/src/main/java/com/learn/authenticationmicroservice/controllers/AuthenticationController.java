package com.learn.authenticationmicroservice.controllers;

import com.learn.authenticationmicroservice.dtos.*;
import com.learn.authenticationmicroservice.dtos.ResponseStatus;
import com.learn.authenticationmicroservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication/users")
@Slf4j
public class AuthenticationController {

    //    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponseDto<UserDto>> login(@RequestParam("roleType") String roleType,
                                                             @RequestBody UserLoginRequestDto userLoginRequestDto) {
        ResponseEntity<GenericResponseDto<UserDto>> responseEntity =
                ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(roleType, userLoginRequestDto));
        log.info("Returning GenericResponseDto<UserDto>: {}", responseEntity);
        return responseEntity;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponseDto<UserDto>> register(@RequestParam("roleType") String roleType,
                                                                @RequestBody UserSignupRequestDto userSignupRequestDto) {
        log.info("***** register: customerSignupRequestDto: {}", userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signUp(roleType, userSignupRequestDto));
    }

    @GetMapping("/authenticate")
    @Hidden
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> authenticate(@RequestHeader("Authorization") String authHeader) {
        log.info("***** authenticate: authHeader: {}", authHeader);
        log.info("Authenticating user with token");
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.getAuthentication(authHeader));
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponseDto<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        authenticationService.logout(authHeader);
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "Logout " +
                "Successful", null));
    }

}
