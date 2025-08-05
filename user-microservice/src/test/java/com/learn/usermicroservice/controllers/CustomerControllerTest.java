package com.learn.usermicroservice.controllers;

import com.learn.usermicroservice.dtos.*;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testSignUp() throws Exception {
//        CustomerSignupRequestDto signupRequest = new CustomerSignupRequestDto();
//        signupRequest.setFirstName("John");
//        signupRequest.setLastName("Doe");
//        signupRequest.setEmail("john@doe.com");
//        signupRequest.setPassword("password");
//        signupRequest.setPhoneNumber("123456789");

        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();
        when(customerService.createCustomer(
                anyString(), anyString(), anyString(), anyString(), anyString())
        ).thenReturn(customer);
        when(modelMapper.map(eq(customer), eq(CustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(post("/user/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@doe.com\",\"password\":\"password\",\"phoneNumber\":\"123456789\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testLogin() throws Exception {
//        CustomerLoginRequestDto loginRequest = new CustomerLoginRequestDto();
//        loginRequest.setEmail("john@doe.com");
//        loginRequest.setPassword("password");

        Token token = new Token();
        token.setAccessToken("test-token");
        when(customerService.login(anyString(), anyString())).thenReturn(token);

        mockMvc.perform(post("/user/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@doe.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testLogout() throws Exception {
        doNothing().when(customerService).logout(anyString());

        mockMvc.perform(post("/user/customer/logout")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testUpdateProfile() throws Exception {
//        CustomerUpdateRequestDto updateRequest = new CustomerUpdateRequestDto();
//        updateRequest.setFirstName("John");
//        updateRequest.setLastName("Smith");

        Customer customer = new Customer();
        CustomerUpdateResponseDto responseDto = new CustomerUpdateResponseDto();
        when(customerService.updateCustomer(anyString(), anyString(), anyString(), anyString())).thenReturn(customer);
        when(modelMapper.map(eq(customer), eq(CustomerUpdateResponseDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/user/customer/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john@doe.com\",\"phoneNumber\":\"1112223333\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testValidateToken() throws Exception {
        doNothing().when(customerService).validateToken(anyString());

        mockMvc.perform(post("/user/customer/validate-token")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testGetAuthentication() throws Exception {
        AuthenticationDto authDto = new AuthenticationDto();
        when(customerService.getAuthentication(anyString())).thenReturn(authDto);

        mockMvc.perform(get("/user/customer/authenticate")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }
}
