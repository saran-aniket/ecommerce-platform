package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.AuthenticationDto;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.exceptions.UnauthorizedException;
import com.learn.usermicroservice.factories.UserProfileFactory;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.services.UserRoleService;
import com.learn.usermicroservice.utilities.USConstants;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAuthServiceImplTest {

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private TokenBlackListServiceImpl tokenBlackListService;

    @Mock
    private UserProfileFactory userProfileFactory;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        String email = "user@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String roleType = USConstants.CUSTOMER_ROLE;
        String generatedToken = "jwt-token";
        long expiresAtMillis = System.currentTimeMillis() + 10000;

        UserRole userRole = new UserRole();
        ApplicationUser user = new ApplicationUser();
        user.setPassword(encodedPassword);

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setApplicationUser(user);
        applicationUserRole.setIsActive(true);

        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.valueOf(roleType));
        when(userRoleService.getUserRoleByName(roleType)).thenReturn(Optional.of(userRole));
        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtService.generateToken(any(Map.class))).thenReturn(generatedToken);
        when(userService.getUserByRoleTypeAndEmail(anyString(), any(UserRoleType.class))).thenReturn(Optional.of(applicationUserRole));
        Date expiration = new Date(expiresAtMillis);
        when(jwtService.extractExpiration(generatedToken)).thenReturn(expiration);

        Token result = userAuthService.login(email, password, roleType);

        assertNotNull(result);
        assertEquals(generatedToken, result.getAccessToken());
        assertEquals(expiresAtMillis, result.getExpiresAt());
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void login_shouldThrowInvalidCredentials_whenUserNotFound() {
        String email = "nouser@example.com";
        String password = "password";
        String roleType = USConstants.CUSTOMER_ROLE;
        UserRole userRole = new UserRole();

        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.valueOf(roleType));
        when(userRoleService.getUserRoleByName(roleType)).thenReturn(Optional.of(userRole));
        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () -> {
            userAuthService.login(email, password, roleType);
        });
    }

    @Test
    void login_shouldThrowInvalidCredentials_whenUserRoleNotPresent() {
        String email = "user@example.com";
        String password = "password";
        String roleType = USConstants.SELLER_ROLE;
        UserRole requiredRole = new UserRole();
        ApplicationUser user = new ApplicationUser();

        when(userService.getUserByRoleTypeAndEmail(roleType, UserRoleType.valueOf(roleType))).thenReturn(Optional.empty());
        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.valueOf(roleType));
        when(userRoleService.getUserRoleByName(roleType)).thenReturn(Optional.of(requiredRole));
        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialException.class, () -> {
            userAuthService.login(email, password, roleType);
        });
    }

    @Test
    void login_shouldThrowInvalidCredentials_whenUserRolePresentAndNotActive() {
        String email = "user@example.com";
        String password = "password";
        String roleType = USConstants.SELLER_ROLE;
        UserRole requiredRole = new UserRole();
        ApplicationUser user = new ApplicationUser();

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setUserRole(requiredRole);
        applicationUserRole.setApplicationUser(user);
        applicationUserRole.setIsActive(false);

        when(userService.getUserByRoleTypeAndEmail(roleType, UserRoleType.valueOf(roleType))).thenReturn(Optional.of(applicationUserRole));
        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.valueOf(roleType));
        when(userRoleService.getUserRoleByName(roleType)).thenReturn(Optional.of(requiredRole));
        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialException.class, () -> {
            userAuthService.login(email, password, roleType);
        });
    }

    @Test
    void login_shouldThrowInvalidCredentials_whenWrongPassword() {
        String email = "user@example.com";
        String password = "wrong";
        String encodedPassword = "encoded";
        String roleType = USConstants.SELLER_ROLE;
        UserRole userRole = new UserRole();
        ApplicationUser user = new ApplicationUser();
        user.setPassword(encodedPassword);

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setApplicationUser(user);
        applicationUserRole.setIsActive(true);

        when(userService.getUserByRoleTypeAndEmail(roleType, UserRoleType.valueOf(roleType))).thenReturn(Optional.of(applicationUserRole));
        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.valueOf(roleType));
        when(userRoleService.getUserRoleByName(roleType)).thenReturn(Optional.of(userRole));
        when(applicationUserRepository.findApplicationUserByEmail(email)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(InvalidCredentialException.class, () -> {
            userAuthService.login(email, password, roleType);
        });
    }

    @Test
    void logout_shouldAddTokenToBlackList() {
        String token = "anyToken";
        doNothing().when(tokenBlackListService).addTokenToBlackList(token);

        userAuthService.logout(token);

        verify(tokenBlackListService, times(1)).addTokenToBlackList(token);
    }

    @Test
    void validateToken_shouldDelegateToJwtService() {
        String token = "jwtToken";
        doNothing().when(jwtService).validateToken(token);

        userAuthService.validateToken(token);

        verify(jwtService, times(1)).validateToken(token);
    }

    @Test
    void getAuthentication_shouldReturnAuthenticationDto_whenValidBearerToken() {
        String authorizationHeader = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        Claims claims = mock(Claims.class);
        AuthenticationDto expected = mock(AuthenticationDto.class);

        when(tokenBlackListService.isTokenBlackListed(jwtToken)).thenReturn(false);
        doNothing().when(jwtService).validateToken(jwtToken);
        when(jwtService.extractAllClaims(jwtToken)).thenReturn(claims);
        try (MockedStatic<AuthenticationDto> authenticationDtoMockedStatic = mockStatic(AuthenticationDto.class)) {
            authenticationDtoMockedStatic.when(() -> AuthenticationDto.from(claims)).thenReturn(expected);

            AuthenticationDto result = userAuthService.getAuthentication(authorizationHeader);
            assertEquals(expected, result);
        }
    }

    @Test
    void getAuthentication_shouldThrowUnauthorized_whenHeaderIsNull() {
        assertThrows(UnauthorizedException.class, () -> {
            userAuthService.getAuthentication(null);
        });
    }

    @Test
    void getAuthentication_shouldThrowUnauthorized_whenHeaderDoesNotStartWithBearer() {
        assertThrows(UnauthorizedException.class, () -> {
            userAuthService.getAuthentication("InvalidHeader abc");
        });
    }

    @Test
    void getAuthentication_shouldThrowAuthCredentialsNotFound_whenTokenBlacklisted() {
        String jwtToken = "blacklisted.jwt.token";
        String header = "Bearer " + jwtToken;
        when(tokenBlackListService.isTokenBlackListed(jwtToken)).thenReturn(true);

        assertThrows(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class, () -> {
            userAuthService.getAuthentication(header);
        });
    }
}