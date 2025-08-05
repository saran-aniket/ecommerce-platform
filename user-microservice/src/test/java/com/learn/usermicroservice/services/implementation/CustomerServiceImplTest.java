package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.AuthenticationDto;
import com.learn.usermicroservice.exceptions.DuplicateEmailException;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.exceptions.UnauthorizedException;
import com.learn.usermicroservice.exceptions.UserRoleDoesNotExistException;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.repositories.CustomerRepository;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import com.learn.usermicroservice.utility.TestDataSetup;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private TokenBlackListServiceImpl tokenBlackListService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void createCustomer_successful() {
        UserRole role = new UserRole();
        role.setName("ROLE_CUSTOMER");
        when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(anyString(), any())).thenReturn(Optional.empty());
        when(applicationUserRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Customer result = customerService.createCustomer("John", "Doe", "john@test.com", "pass", "123456789");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_duplicateEmail() {
        UserRole role = new UserRole();
        when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(anyString(), any())).thenReturn(Optional.of(new ApplicationUser()));

        assertThrows(DuplicateEmailException.class, () ->
                customerService.createCustomer("John", "Doe", "john@test.com", "pass", "123456789")
        );
    }

    @Test
    void updateCustomer_successful() {
        UserRole role = new UserRole();
        role.setName("ROLE_CUSTOMER");
        ApplicationUser user = new ApplicationUser();
        user.setUsername("email");
        Customer customer = new Customer();
        customer.setApplicationUser(user);
        when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(anyString(), any())).thenReturn(Optional.of(user));
        when(customerRepository.findCustomerByApplicationUser(user)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(applicationUserRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Customer result = customerService.updateCustomer("Jane", "Smith", "email", "987654321");

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void updateCustomer_noCustomerFound() {
        UserRole role = new UserRole();
        ApplicationUser user = new ApplicationUser();
        when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(anyString(), any())).thenReturn(Optional.of(user));
        when(customerRepository.findCustomerByApplicationUser(user)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () ->
                customerService.updateCustomer("Jane", "Smith", "email", "987654321")
        );
    }

    @Test
    void updateCustomer_userRoleMissing() {
        when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.empty());

        assertThrows(UserRoleDoesNotExistException.class, () ->
                customerService.updateCustomer("Jane", "Smith", "email", "987654321")
        );
    }

    @Test
    void login_successful() {
        List<UserRole> userRoleList = TestDataSetup.getCustomerUserRole();
        ApplicationUser appUser = TestDataSetup.getSingleApplicationUser();
        appUser.setUserRoles(userRoleList);
        appUser.setPassword("encoded");
        Customer customer = TestDataSetup.getSingleCustomer();
        customer.setApplicationUser(appUser);
        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(customerRepository.findCustomerByApplicationUser(appUser)).thenReturn(Optional.of(customer));
        when(jwtService.generateToken(any())).thenReturn("test.jwt.token");
        when(jwtService.extractExpiration(anyString())).thenReturn(new Date(System.currentTimeMillis() + 60000));

        var token = customerService.login("mail", "plaintext");

        assertNotNull(token.getAccessToken());
        assertTrue(token.getExpiresAt() > System.currentTimeMillis());
    }

    @Test
    void login_invalidEmail() {
        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () ->
                customerService.login("mail", "plaintext")
        );
    }

    @Test
    void login_wrongPassword() {
        ApplicationUser appUser = new ApplicationUser();
        appUser.setPassword("encoded");
        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialException.class, () ->
                customerService.login("mail", "plaintext")
        );
    }

    @Test
    void login_noCustomer() {
        ApplicationUser appUser = new ApplicationUser();
        appUser.setPassword("encoded");
        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(customerRepository.findCustomerByApplicationUser(appUser)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () ->
                customerService.login("mail", "plaintext")
        );
    }

    @Test
    void logout_callsBlacklist() {
        doNothing().when(tokenBlackListService).addTokenToBlackList(anyString());
        customerService.logout("sometoken");
        verify(tokenBlackListService).addTokenToBlackList("sometoken");
    }

    @Test
    void validateToken_callsJwtService() {
        doNothing().when(jwtService).validateToken(anyString());
        customerService.validateToken("abc");
        verify(jwtService).validateToken("abc");
    }

    @Test
    void getAuthentication_successful() {
        String authHeader = "Bearer my.jwt.token";
        String jwt = "my.jwt.token";
        Claims claims = mock(Claims.class);
        when(tokenBlackListService.isTokenBlackListed(jwt)).thenReturn(false);
        doNothing().when(jwtService).validateToken(jwt);
        when(jwtService.extractAllClaims(jwt)).thenReturn(claims);
        AuthenticationDto dto = mock(AuthenticationDto.class);
        try (MockedStatic<AuthenticationDto> mockStatic = mockStatic(AuthenticationDto.class)) {
            mockStatic.when(() -> AuthenticationDto.from(claims)).thenReturn(dto);
            AuthenticationDto result = customerService.getAuthentication(authHeader);
            assertEquals(dto, result);
        }
    }

    @Test
    void getAuthentication_unauthorizedHeader() {
        assertThrows(UnauthorizedException.class, () ->
                customerService.getAuthentication(null)
        );
        assertThrows(UnauthorizedException.class, () ->
                customerService.getAuthentication("InvalidPrefix token")
        );
    }

    @Test
    void getAuthentication_tokenBlacklisted() {
        String jwt = "my.jwt.token";
        when(tokenBlackListService.isTokenBlackListed(jwt)).thenReturn(true);
        String authHeader = "Bearer my.jwt.token";
        assertThrows(AuthenticationCredentialsNotFoundException.class, () ->
                customerService.getAuthentication(authHeader)
        );
    }

}