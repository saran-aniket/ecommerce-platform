package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.exceptions.UserRoleDoesNotExistException;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import com.learn.usermicroservice.utilities.USConstants;
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
        String name = USConstants.ADMIN_ROLE;
        UserRole userRole = new UserRole();
        userRole.setName(name);

        when(userRoleRepository.findUserRoleByName(name)).thenReturn(Optional.of(userRole));

        UserRole result = userRoleService.getUserRoleByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(userRoleRepository, times(1)).findUserRoleByName(name);
    }

    @Test
    void getUserRoleByName_shouldThrowExceptionIfNotFound() {
        String name = "NOTFOUND";
        when(userRoleRepository.findUserRoleByName(name)).thenReturn(Optional.empty());

        assertThrows(UserRoleDoesNotExistException.class, () -> {
            userRoleService.getUserRoleByName(name);
        });
        verify(userRoleRepository, times(1)).findUserRoleByName(name);
    }
}