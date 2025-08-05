package com.learn.usermicroservice.controllers;

import com.learn.usermicroservice.dtos.*;
import com.learn.usermicroservice.dtos.ResponseStatus;
import com.learn.usermicroservice.exceptions.UnauthorizedException;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.services.CustomerService;
import com.learn.usermicroservice.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<GenericResponseDto<CustomerDto>> signUp(@RequestBody CustomerSignupRequestDto customerSignRequestDto) {
        log.info("User Service | signup");
        CustomerDto customerDto = new CustomerDto();
        Customer customer = customerService.createCustomer(customerSignRequestDto.getFirstName(),
                customerSignRequestDto.getLastName(), customerSignRequestDto.getEmail(),
                customerSignRequestDto.getPassword(), customerSignRequestDto.getPhoneNumber());
        customerDto = modelMapper.map(customer, CustomerDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", customerDto));
    }


    @PostMapping("/login")
    public ResponseEntity<GenericResponseDto<CustomerDto>> login(@RequestBody CustomerLoginRequestDto customerLoginRequestDto) {
        log.info("User Service | login");
        CustomerDto customerDto = new CustomerDto();
        Token token = customerService.login(customerLoginRequestDto.getEmail(),
                customerLoginRequestDto.getPassword());
        customerDto.setEmail(customerLoginRequestDto.getEmail());
        customerDto.setToken(token.getAccessToken());
        log.info("Token: {}", token.getAccessToken());
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", customerDto));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<GenericResponseDto<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        customerService.logout(authHeader.replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", null));
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<GenericResponseDto<CustomerUpdateResponseDto>> updateProfile(@RequestBody CustomerUpdateRequestDto customerUpdateRequestDto) {
        Customer customer = customerService.updateCustomer(
                customerUpdateRequestDto.getFirstName(),
                customerUpdateRequestDto.getLastName(),
                customerUpdateRequestDto.getEmail(),
                customerUpdateRequestDto.getPhoneNumber());
        CustomerUpdateResponseDto customerUpdateResponseDto = modelMapper.map(customer, CustomerUpdateResponseDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", customerUpdateResponseDto));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<GenericResponseDto<Void>> validateToken(@RequestHeader("Authorization") String authHeader) {
        customerService.validateToken(authHeader.replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", null));
    }

    @GetMapping("/authenticate")
    public ResponseEntity<GenericResponseDto<AuthenticationDto>> getAuthentication(@RequestHeader("Authorization") String authHeader) throws UnauthorizedException {
        log.info("authHeader: {}", authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(GenericResponseDto.GenericResponseDtoFrom(ResponseStatus.SUCCESS, "", customerService.getAuthentication(authHeader)));
    }
}
