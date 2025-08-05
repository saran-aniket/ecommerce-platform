package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.utility.TestDataSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserRoleRepositoryTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    private List<UserRole> userRole;

    @BeforeEach
    void setUp() {
        userRole = TestDataSetup.getCustomerUserRole();
    }

    @Test
    void findUserRoleByName_Success() {
        Mockito.when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.ofNullable(userRole.getFirst()));

        Optional<UserRole> userRole1 = userRoleRepository.findUserRoleByName("ROLE_CUSTOMER");
        Mockito.verify(userRoleRepository).findUserRoleByName("ROLE_CUSTOMER");

        assertTrue(userRole1.isPresent());
        assertEquals(userRole.getFirst().getName(), userRole1.get().getName());
    }

    @Test
    void findUserRoleByName_Failure() {
        Mockito.when(userRoleRepository.findUserRoleByName("ROLE_CUSTOMER")).thenReturn(Optional.empty());

        Optional<UserRole> userRole1 = userRoleRepository.findUserRoleByName("ROLE_CUSTOMER");
        Mockito.verify(userRoleRepository).findUserRoleByName("ROLE_CUSTOMER");

        assertTrue(userRole1.isEmpty());
    }
}