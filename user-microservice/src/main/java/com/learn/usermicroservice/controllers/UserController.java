package com.learn.usermicroservice.controllers;

import com.learn.usermicroservice.dtos.*;
import com.learn.usermicroservice.dtos.ResponseStatus;
import com.learn.usermicroservice.exceptions.UnauthorizedException;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.services.UserAuthService;
import com.learn.usermicroservice.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserAuthService userAuthService;

    public UserController(UserService userService, ModelMapper modelMapper, UserAuthService userAuthService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/auth/signup")
    @Hidden
    public ResponseEntity<GenericResponseDto<UserDto>> signUp(@RequestParam(name = "roleType") String roleType,
                                                              @RequestBody UserSignupRequestDto userSignupRequestDto) {
        log.info("User Service | signup");
        ApplicationUser applicationUser = userService.createUser(userSignupRequestDto, roleType);
        UserDto userDto = modelMapper.map(applicationUser, UserDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", userDto));
    }


    @PostMapping("/auth/login")
    @Hidden
    public ResponseEntity<GenericResponseDto<UserDto>> login(@RequestParam("roleType") String roleType,
                                                             @RequestBody UserLoginRequestDto userLoginRequestDto) {
        log.info("User Service | login");
        UserDto userDto = new UserDto();
        Token token = userAuthService.login(userLoginRequestDto.getEmail(),
                userLoginRequestDto.getPassword(), roleType);
        userDto.setEmail(userLoginRequestDto.getEmail());
        userDto.setToken(token.getAccessToken());
        log.info("Token: {}", token.getAccessToken());
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", userDto));
    }

    @PostMapping("/auth/logout")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    @Hidden
    public ResponseEntity<GenericResponseDto<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        userAuthService.logout(authHeader.replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", null));
    }


    @PostMapping("/auth/validate-token")
    @Hidden
    public ResponseEntity<GenericResponseDto<Void>> validateToken(@RequestHeader("Authorization") String authHeader) {
        userAuthService.validateToken(authHeader.replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", null));
    }

    @GetMapping("/auth")
    @Hidden
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> getAuthentication(@RequestHeader("Authorization") String authHeader) throws UnauthorizedException {
        log.info("authHeader: {}", authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", userAuthService.getAuthentication(authHeader)));
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<GenericResponseDto<UserUpdateResponseDto>> updateProfile(@RequestParam("roleType") String roleType, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        ApplicationUser applicationUser = userService.updateUser(userUpdateRequestDto, roleType);
        UserUpdateResponseDto userUpdateResponseDto = modelMapper.map(applicationUser, UserUpdateResponseDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", userUpdateResponseDto));
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<GenericResponseDto<Void>> deleteProfile(@RequestParam("roleType") String roleType, @RequestBody UserDeleteRequestDto userDeleteRequestDto) {
        userService.deleteUser(userDeleteRequestDto.getEmail(), roleType);
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", null));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER')")
    @Hidden
    public ResponseEntity<GenericResponseDto<GetUserResponseDto>> getUserProfileById(@RequestParam("roleType") String roleType,
                                                                                     @RequestParam("userId") String userId) {
        return ResponseEntity.ok().body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "",
                GetUserResponseDto.from(userService.getUserByIdAndRoleType(userId, roleType))));
    }

}
