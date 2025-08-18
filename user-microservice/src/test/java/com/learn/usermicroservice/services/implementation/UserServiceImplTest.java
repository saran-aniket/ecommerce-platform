package com.learn.usermicroservice.services.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.exceptions.DuplicateEmailException;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.exceptions.UserRoleDoesNotExistException;
import com.learn.usermicroservice.factories.UserProfileFactory;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.repositories.CustomerRepository;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import com.learn.usermicroservice.services.UserProfileService;
import com.learn.usermicroservice.services.UserRoleService;
import com.learn.usermicroservice.utilities.USConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

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
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserProfileFactory userProfileFactory;
    @Mock
    private UserProfileService userProfileService;
    @Captor
    private ArgumentCaptor<ApplicationUser> userCaptor;

    private Map<String, UserProfileService> userProfileServiceMap;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userProfileServiceMap = new HashMap<>();
        userProfileServiceMap.put("defaultService", userProfileService);

        userService = new UserServiceImpl(
                customerRepository,
                applicationUserRepository,
                userRoleService,
                userProfileFactory,
                userProfileServiceMap
        );
    }

    @Test
    void createUser_newUser_success() {
        UserSignupRequestDto signupDto = new UserSignupRequestDto();
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");
        signupDto.setFirstName("John");
        signupDto.setLastName("Doe");
        signupDto.setPhoneNumber("1234567890");
        UserRole userRole = new UserRole();
        userRole.setName(UserRoleType.ROLE_CUSTOMER.name());
        ApplicationUser user = new ApplicationUser();
        user.setUserRoles(Collections.singletonList(userRole));

        when(applicationUserRepository.findApplicationUserByEmail(signupDto.getEmail())).thenReturn(Optional.empty());
        when(userProfileFactory.getUserProfileServiceBeanName()).thenReturn("defaultService");
        when(userProfileFactory.getConvertedUserSignupRequestDto(signupDto)).thenReturn(signupDto);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashed");
        doThrow(new UserRoleDoesNotExistException("User Role does not exist")).when(userRoleService).getUserRoleByName(null);
        when(userRoleService.createUserRole(any(UserRole.class))).thenReturn(userRole);
        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.ROLE_CUSTOMER);
        when(userProfileService.createUserProfile(any(UserSignupRequestDto.class), any(ApplicationUser.class))).thenReturn(user);

        user = userService.createUser(signupDto, USConstants.CUSTOMER_ROLE);

        assertNotNull(user);
    }

    @Test
    void createUser_existingUserWithRole_throwsDuplicateEmailException() {
        UserSignupRequestDto signupDto = new UserSignupRequestDto();
        signupDto.setEmail("test@example.com");

        UserRole userRole = new UserRole();
        userRole.setName(UserRoleType.ROLE_CUSTOMER.name());
        List<UserRole> roles = new ArrayList<>();
        roles.add(userRole);

        ApplicationUser existingUser = new ApplicationUser();
        existingUser.setUserRoles(roles);

        when(applicationUserRepository.findApplicationUserByEmail(signupDto.getEmail()))
                .thenReturn(Optional.of(existingUser));
        when(userProfileFactory.getUserProfileServiceBeanName()).thenReturn("defaultService");
        when(userRoleService.getUserRoleByName(anyString())).thenReturn(userRole);
        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.ROLE_CUSTOMER);

        assertThrows(DuplicateEmailException.class,
                () -> userService.createUser(signupDto, USConstants.CUSTOMER_ROLE));
    }

    @Test
    void updateUser_success() {
        UserUpdateRequestDto updateDto = new UserUpdateRequestDto();
        updateDto.setEmail("test@example.com");
        updateDto.setFirstName("John");
        updateDto.setLastName("Doe");
        updateDto.setPhoneNumber("1234567890");

        ApplicationUser user = new ApplicationUser();
        user.setEmail("test@example.com");

        when(userRoleService.getUserRoleByName(USConstants.CUSTOMER_ROLE)).thenReturn(new UserRole());
        when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(eq("test@example.com"), anyList()))
                .thenReturn(Optional.of(user));
        when(userProfileFactory.getUserProfileServiceBeanName()).thenReturn("defaultService");
        when(userProfileFactory.getConvertedUserUpdateRequestDto(updateDto)).thenReturn(updateDto);
        when(userProfileService.updateUserProfile(any(UserUpdateRequestDto.class), any(ApplicationUser.class))).thenReturn(user);

        ApplicationUser result = userService.updateUser(updateDto, USConstants.CUSTOMER_ROLE);
        assertNotNull(result);
        verify(userProfileService).updateUserProfile(updateDto, user);
    }

    @Test
    void deleteUser_shouldDeactivateUser() {
        String email = "test@example.com";
        String roleType = USConstants.CUSTOMER_ROLE;

        ApplicationUser user = new ApplicationUser();
        user.setEmail(email);
        user.setActive(true);

        when(userRoleService.getUserRoleByName(USConstants.CUSTOMER_ROLE)).thenReturn(new UserRole());
        when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(eq(email), anyList()))
                .thenReturn(Optional.of(user));

        userService.deleteUser(email, roleType);

        assertFalse(user.isActive());
        verify(applicationUserRepository).save(user);
    }

    @Test
    void getUserByEmail_existingUser_returnsUser() {
        ApplicationUser user = new ApplicationUser();
        when(applicationUserRepository.findApplicationUserByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        ApplicationUser result = userService.getUserByEmail("test@example.com");

        assertEquals(user, result);
    }

    @Test
    void getUserByEmail_nonExistingUser_throwsException() {
        when(applicationUserRepository.findApplicationUserByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () -> userService.getUserByEmail("notfound@example.com"));
    }

    @Test
    void getAllUsers_returnsActiveUsers() {
        List<ApplicationUser> users = Arrays.asList(new ApplicationUser(), new ApplicationUser());
        when(applicationUserRepository.findAllByIsActiveTrue()).thenReturn(users);
        List<ApplicationUser> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }
}