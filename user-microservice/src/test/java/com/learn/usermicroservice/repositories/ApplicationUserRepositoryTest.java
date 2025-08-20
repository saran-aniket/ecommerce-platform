package com.learn.usermicroservice.repositories;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.utility.TestDataSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ApplicationUserRepositoryTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    private ApplicationUser applicationUser;

    @BeforeEach
    void setUp() {
        applicationUser = TestDataSetup.getSingleApplicationUser();
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