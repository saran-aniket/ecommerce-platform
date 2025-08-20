package com.learn.usermicroservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.usermicroservice.dtos.*;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.services.UserAuthService;
import com.learn.usermicroservice.services.UserService;
import com.learn.usermicroservice.utilities.USConstants;
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

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testSignUp() throws Exception {
        UserSignupRequestDto signupRequest = new UserSignupRequestDto();
        ApplicationUser applicationUser = new ApplicationUser();
        UserDto userDto = new UserDto();

        given(userService.createUser(any(UserSignupRequestDto.class), eq(USConstants.CUSTOMER_ROLE))).willReturn(applicationUser);
        given(modelMapper.map(applicationUser, UserDto.class)).willReturn(userDto);

        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .param("roleType", USConstants.CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@doe.com\",\"password\":\"password\",\"phoneNumber\":\"123456789\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testLogin() throws Exception {
        UserLoginRequestDto loginRequest = new UserLoginRequestDto();
        loginRequest.setEmail("john@doe.com");
        loginRequest.setPassword("password");

        Token token = new Token();
        token.setAccessToken("test-token");

        when(userAuthService.login(anyString(), anyString(), eq(USConstants.CUSTOMER_ROLE))).thenReturn(token);

        mockMvc.perform(post("/api/v1/user/auth/login")
                        .param("roleType", USConstants.CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@doe.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testLogout() throws Exception {
        doNothing().when(userAuthService).logout(anyString());

        mockMvc.perform(post("/api/v1/user/auth/logout")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testUpdateProfile() throws Exception {
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto();
        ApplicationUser applicationUser = new ApplicationUser();
        UserUpdateResponseDto userUpdateResponseDto = new UserUpdateResponseDto();

        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(USConstants.CUSTOMER_ROLE))).thenReturn(applicationUser);
        when(modelMapper.map(applicationUser, UserUpdateResponseDto.class)).thenReturn(userUpdateResponseDto);

        mockMvc.perform(patch("/api/v1/user/profile")
                        .param("roleType", USConstants.CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Jane\",\"lastName\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testValidateToken() throws Exception {
        doNothing().when(userAuthService).validateToken(anyString());

        mockMvc.perform(post("/api/v1/user/auth/validate-token")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void testGetAuthentication() throws Exception {
        AuthenticationDto authenticationDto = new AuthenticationDto();
        when(userAuthService.getAuthentication(anyString())).thenReturn(authenticationDto);

        mockMvc.perform(get("/api/v1/user/auth")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    @Test
    void deleteProfile_shouldReturnOk() throws Exception {
        UserDeleteRequestDto deleteDto = new UserDeleteRequestDto();
        deleteDto.setEmail("delete@example.com");
        doNothing().when(userService).deleteUser(anyString(), anyString());

        mockMvc.perform(delete("/api/v1/user/profile")
                        .param("roleType", USConstants.CUSTOMER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\" : \"jane@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void getUserProfileById_shouldReturnOk() throws Exception {
        UUID uuid = UUID.randomUUID();
        String roleType = "ROLE_CUSTOMER";
        ApplicationUser applicationUser = new ApplicationUser();

        when(userService.getUserByIdAndRoleType(String.valueOf(uuid), roleType)).thenReturn(applicationUser);

        mockMvc.perform(get("/api/v1/user/profile")
                        .param("roleType", roleType)
                        .param("userId", String.valueOf(uuid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

}