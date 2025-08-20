package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.exceptions.DuplicateEmailException;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.exceptions.UserRoleDoesNotExistException;
import com.learn.usermicroservice.factories.UserProfileFactory;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.repositories.ApplicationUserRoleRepository;
import com.learn.usermicroservice.services.UserProfileService;
import com.learn.usermicroservice.services.UserRoleService;
import com.learn.usermicroservice.utilities.USConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private UserProfileFactory userProfileFactory;
    @Mock
    private UserProfileService userProfileService;

    @Mock
    private Map<String, UserProfileService> userProfileServiceMap;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ApplicationUserRoleRepository applicationUserRoleRepository;

    @Mock
    private ApplicationUserRoleServiceImpl applicationUserRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userProfileServiceMap = new HashMap<>();
        userProfileServiceMap.put("defaultService", userProfileService);

        userService = new UserServiceImpl(
                applicationUserRepository,
                userRoleService,
                userProfileFactory,
                userProfileServiceMap,
                applicationUserRoleRepository,
                applicationUserRoleService
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
        userRole.setUserRoleType(UserRoleType.ROLE_CUSTOMER);
        ApplicationUser user = new ApplicationUser();

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setApplicationUser(user);
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setIsActive(true);

        when(applicationUserRoleService.saveApplicationUserRole(any(ApplicationUser.class), any(UserRole.class), anyBoolean())).thenReturn(applicationUserRole);
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
        userRole.setUserRoleType(UserRoleType.ROLE_CUSTOMER);

        ApplicationUser existingUser = new ApplicationUser();

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setApplicationUser(existingUser);
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setIsActive(true);

        existingUser.getApplicationUserRoles().add(applicationUserRole);

        when(applicationUserRepository.findApplicationUserByEmail(signupDto.getEmail()))
                .thenReturn(Optional.of(existingUser));
        when(userProfileFactory.getUserProfileServiceBeanName()).thenReturn("defaultService");
        when(userRoleService.getUserRoleByName(anyString())).thenReturn(Optional.of(userRole));
        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.ROLE_CUSTOMER);
        when(applicationUserRoleRepository.findApplicationUserRolesByApplicationUserAndUserRole(any(ApplicationUser.class), any(UserRole.class))).thenReturn(Optional.of(applicationUserRole));

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

        UserRole userRole = new UserRole();
        userRole.setUserRoleType(UserRoleType.ROLE_CUSTOMER);

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setApplicationUser(user);
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setIsActive(true);

        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(applicationUserRoleRepository.findApplicationUserRolesByApplicationUserAndUserRole(any(ApplicationUser.class), any(UserRole.class))).thenReturn(Optional.of(applicationUserRole));
        when(userRoleService.getUserRoleByName(anyString())).thenReturn(Optional.of(userRole));
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

        UserRole userRole = new UserRole();
        userRole.setUserRoleType(UserRoleType.ROLE_CUSTOMER);

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setApplicationUser(user);
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setIsActive(true);

        ApplicationUserRole updatedApplicationUserRole = new ApplicationUserRole();
        updatedApplicationUserRole.setIsActive(false);

        when(applicationUserRepository.findApplicationUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(applicationUserRoleRepository.findApplicationUserRolesByApplicationUserAndUserRole(any(ApplicationUser.class), any(UserRole.class))).thenReturn(Optional.of(applicationUserRole));
        when(userRoleService.getUserRoleByName(USConstants.CUSTOMER_ROLE)).thenReturn(Optional.of(userRole));
        when(applicationUserRoleRepository.save(any(ApplicationUserRole.class))).thenReturn(updatedApplicationUserRole);

        userService.deleteUser(email, roleType);

        assertFalse(applicationUserRole.getIsActive());
        verify(applicationUserRoleRepository).save(applicationUserRole);
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
    void getAllActiveUsersByRoleType_returnsActiveUsers() {
        List<ApplicationUser> users = Arrays.asList(new ApplicationUser(),
                new ApplicationUser());

        when(userProfileFactory.getUserRoleType()).thenReturn(UserRoleType.ROLE_CUSTOMER);
        when(applicationUserRoleService.getAllApplicationUsersByRoleType(any(UserRoleType.class))).thenReturn(users);
        List<ApplicationUser> result = userService.getAllActiveUsersByRoleType(USConstants.CUSTOMER_ROLE);
        assertEquals(2, result.size());
    }
}