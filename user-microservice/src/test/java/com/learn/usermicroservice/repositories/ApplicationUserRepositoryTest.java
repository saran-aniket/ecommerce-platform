package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.utility.TestDataSetup;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserRepositoryTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    private ApplicationUser applicationUser;
    private List<UserRole> userRoleList;

    @BeforeEach
    void setUp() {
        applicationUser = TestDataSetup.getSingleApplicationUser();
        userRoleList = TestDataSetup.getCustomerUserRole();
    }

    @Test
    void findApplicationUserByEmailAndUserRoles_Success() {
        Mockito.when(applicationUserRepository.findApplicationUserByEmailAndUserRoles(applicationUser.getEmail(),
                userRoleList)).thenReturn(Optional.ofNullable(applicationUser));
        Optional<ApplicationUser> applicationUser1 =
                applicationUserRepository.findApplicationUserByEmailAndUserRoles(applicationUser.getEmail(),
                userRoleList);
        Mockito.verify(applicationUserRepository).findApplicationUserByEmailAndUserRoles(applicationUser.getEmail(),userRoleList);
        assertTrue(applicationUser1.isPresent());
        assertEquals(applicationUser.getEmail(), applicationUser1.get().getEmail());
    }

    @Test
    void findApplicationUserByEmailAndUserRoles_Failure() {
        Optional<ApplicationUser> applicationUser1 = applicationUserRepository.findApplicationUserByEmailAndUserRoles(
                "testfail" +
                        "@mailtest.com",
                userRoleList);
        assertEquals(applicationUser1, Optional.empty());
    }

    @Test
    void findApplicationUserByEmail_Success() {
        Mockito.when(applicationUserRepository.findApplicationUserByEmail(applicationUser.getEmail())).thenReturn(Optional.ofNullable(applicationUser));
        Optional<ApplicationUser> applicationUser1 =
                applicationUserRepository.findApplicationUserByEmail(applicationUser.getEmail());
        assertTrue(applicationUser1.isPresent());
        assertEquals(applicationUser.getEmail(), applicationUser1.get().getEmail());
    }

    @Test
    void findApplicationUserByEmail_Failure() {
        Mockito.when(applicationUserRepository.findApplicationUserByEmail(applicationUser.getEmail())).thenReturn(Optional.empty());

        Optional<ApplicationUser> applicationUser1 =
                applicationUserRepository.findApplicationUserByEmail(applicationUser.getEmail());
        assertTrue(applicationUser1.isEmpty());
    }
}