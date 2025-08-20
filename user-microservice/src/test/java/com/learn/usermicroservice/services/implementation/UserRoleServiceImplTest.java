package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserRole_shouldSaveAndReturnUserRole() {
        UserRole userRole = new UserRole();
        when(userRoleRepository.save(userRole)).thenReturn(userRole);

        UserRole result = userRoleService.createUserRole(userRole);

        assertNotNull(result);
        assertEquals(userRole, result);
        verify(userRoleRepository, times(1)).save(userRole);
    }

    @Test
    void getUserRoleByName_shouldReturnUserRoleIfPresent() {
        String name = "ROLE_ADMIN";
        UserRole userRole = new UserRole();
        userRole.setUserRoleType(UserRoleType.ROLE_ADMIN);

        when(userRoleRepository.findUserRoleByUserRoleType(UserRoleType.ROLE_ADMIN)).thenReturn(Optional.of(userRole));

        Optional<UserRole> result = userRoleService.getUserRoleByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getUserRoleType().name());
        verify(userRoleRepository, times(1)).findUserRoleByUserRoleType(UserRoleType.ROLE_ADMIN);
    }

    @Test
    void getUserRoleByName_shouldThrowExceptionIfNotFound() {
        String name = "NOTFOUND";
        when(userRoleRepository.findUserRoleByUserRoleType(any(UserRoleType.class))).thenReturn(Optional.empty());

        assertTrue(userRoleService.getUserRoleByName(UserRoleType.ROLE_ADMIN.name()).isEmpty());
        verify(userRoleRepository, times(1)).findUserRoleByUserRoleType(UserRoleType.ROLE_ADMIN);
    }
}