package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.entities.ApplicationUserRoleId;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.ApplicationUserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationUserRoleServiceImplTest {

    @Mock
    private ApplicationUserRoleRepository applicationUserRoleRepository;

    @InjectMocks
    private ApplicationUserRoleServiceImpl applicationUserRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllApplicationUsersByRoleType_shouldReturnUsers() {
        UserRoleType roleType = UserRoleType.ROLE_ADMIN;
        ApplicationUser user1 = mock(ApplicationUser.class);
        ApplicationUser user2 = mock(ApplicationUser.class);

        ApplicationUserRole role1 = mock(ApplicationUserRole.class);
        ApplicationUserRole role2 = mock(ApplicationUserRole.class);

        when(role1.getApplicationUser()).thenReturn(user1);
        when(role2.getApplicationUser()).thenReturn(user2);

        List<ApplicationUserRole> roles = List.of(role1, role2);
        when(applicationUserRoleRepository.findApplicationUserRolesByUserRole_UserRoleType(roleType))
                .thenReturn(Optional.of(roles));

        List<ApplicationUser> result = applicationUserRoleService.getAllApplicationUsersByRoleType(roleType);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Set.of(user1, user2)));
        verify(applicationUserRoleRepository, times(1))
                .findApplicationUserRolesByUserRole_UserRoleType(roleType);
    }

    @Test
    void saveApplicationUserRole_shouldSaveAndReturnApplicationUserRole() {
        UUID userUuid = UUID.randomUUID();
        UUID roleUuid = UUID.randomUUID();

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(userUuid);

        UserRole userRole = new UserRole();
        userRole.setId(roleUuid);

        ApplicationUserRoleId applicationUserRoleId = new ApplicationUserRoleId();
        applicationUserRoleId.setApplicationUserId(userUuid);
        applicationUserRoleId.setUserRoleId(roleUuid);

        ApplicationUserRole applicationUserRole = new ApplicationUserRole();
        applicationUserRole.setApplicationUser(applicationUser);
        applicationUserRole.setUserRole(userRole);
        applicationUserRole.setApplicationUserRoleId(applicationUserRoleId);
        applicationUserRole.setIsActive(true);

        when(applicationUserRoleRepository.save(any(ApplicationUserRole.class))).thenReturn(applicationUserRole);

        ApplicationUserRole newApplicationUserRole = applicationUserRoleService.saveApplicationUserRole(applicationUser, userRole, true);

        assertNotNull(newApplicationUserRole);
        assertEquals(newApplicationUserRole.getApplicationUser().getId(), applicationUser.getId());
    }
}