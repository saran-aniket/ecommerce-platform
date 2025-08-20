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
import com.learn.usermicroservice.services.ApplicationUserRoleService;
import com.learn.usermicroservice.services.UserProfileService;
import com.learn.usermicroservice.services.UserRoleService;
import com.learn.usermicroservice.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleService userRoleService;
    private final UserProfileFactory userProfileFactory;
    private final Map<String, UserProfileService> userProfileServiceMap;
    private final ApplicationUserRoleRepository applicationUserRoleRepository;
    private final ApplicationUserRoleService applicationUserRoleService;

    public UserServiceImpl(ApplicationUserRepository applicationUserRepository, UserRoleService userRoleService, UserProfileFactory userProfileFactory, Map<String, UserProfileService> userProfileServiceMap, ApplicationUserRoleRepository applicationUserRoleRepository, ApplicationUserRoleService applicationUserRoleService) {
        this.applicationUserRepository = applicationUserRepository;
        this.userRoleService = userRoleService;
        this.userProfileFactory = userProfileFactory;
        this.userProfileServiceMap = userProfileServiceMap;
        this.applicationUserRoleRepository = applicationUserRoleRepository;
        this.applicationUserRoleService = applicationUserRoleService;
    }


    @Override
    @Transactional
    public ApplicationUser createUser(UserSignupRequestDto userSignupRequestDto, String roleType) {
        userProfileFactory.setUserRoleType(roleType);
        String userProfileServiceBeanName = userProfileFactory.getUserProfileServiceBeanName();
        UserProfileService userProfileService = userProfileServiceMap.get(userProfileServiceBeanName);
        UserSignupRequestDto convertedUserSignupRequestDto = userProfileFactory.getConvertedUserSignupRequestDto(userSignupRequestDto);

        //check if the Role Type exists
        Optional<UserRole> optionalUserRole =
                userRoleService.getUserRoleByName(String.valueOf(userProfileFactory.getUserRoleType()));
        UserRole userRole = new UserRole();
        if (optionalUserRole.isEmpty()) {
            log.error("Role type {} does not exists", userProfileFactory.getUserRoleType().name());
            userRole.setUserRoleType(userProfileFactory.getUserRoleType());
            userRoleService.createUserRole(userRole);
        } else {
            userRole = optionalUserRole.get();
        }

        //Check if the user already exists
        Optional<ApplicationUser> optionalApplicationUser =
                applicationUserRepository.findApplicationUserByEmail(userSignupRequestDto.getEmail());
        if (optionalApplicationUser.isPresent()) {
            log.info("User with email {} already exists", userSignupRequestDto.getEmail());
            //Check if the user already has this role
            UserRole finalUserRole = userRole;
            List<ApplicationUserRole> existingUserRole =
                    optionalApplicationUser.get().getApplicationUserRoles().stream().filter(role -> role.getUserRole().equals(finalUserRole)).toList();
            if (!existingUserRole.isEmpty()) {
                log.info("User with email {} already has role {}", userSignupRequestDto.getEmail(),
                        userRole.getUserRoleType().name());
                if (existingUserRole.getFirst().getIsActive() == true) {
                    throw new DuplicateEmailException("User with email " + userSignupRequestDto.getEmail() + " already exists");
                } else {
                    existingUserRole.getFirst().setIsActive(true);
                    applicationUserRoleRepository.save(existingUserRole.getFirst());
                    return optionalApplicationUser.get();
                }
            } else {
                log.info("User with email {} does not have role {}", userSignupRequestDto.getEmail(), userRole.getUserRoleType().name());
                userProfileService.createUserProfile(userSignupRequestDto, optionalApplicationUser.get());
                applicationUserRoleService.saveApplicationUserRole(optionalApplicationUser.get(), userRole, true);
                return optionalApplicationUser.get();
            }
        } else {
            //create new user
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser = userProfileService.createUserProfile(convertedUserSignupRequestDto, applicationUser);
            return applicationUserRoleService.saveApplicationUserRole(applicationUser, userRole,
                    applicationUser.isActive()).getApplicationUser();
        }
    }

    @Override
    public ApplicationUser updateUser(UserUpdateRequestDto userUpdateRequestDto, String roleType) {
        Optional<ApplicationUserRole> optionalApplicationUserRole = getUserByRoleTypeAndEmail(userUpdateRequestDto.getEmail(), UserRoleType.valueOf(roleType));
        if (optionalApplicationUserRole.isEmpty() || !optionalApplicationUserRole.get().getIsActive()) {
            throw new InvalidCredentialException("Invalid Credentials");
        }
        userProfileFactory.setUserRoleType(roleType);
        String userProfileServiceBeanName = userProfileFactory.getUserProfileServiceBeanName();
        UserProfileService userProfileService = userProfileServiceMap.get(userProfileServiceBeanName);
        UserUpdateRequestDto convertedUserUpdateRequestDto = userProfileFactory.getConvertedUserUpdateRequestDto(userUpdateRequestDto);

        return userProfileService.updateUserProfile(convertedUserUpdateRequestDto,
                optionalApplicationUserRole.get().getApplicationUser());
    }

    @Override
    public void deleteUser(String email, String roleType) {
        Optional<ApplicationUserRole> optionalApplicationUserRole = getUserByRoleTypeAndEmail(email, UserRoleType.valueOf(roleType));
        if (optionalApplicationUserRole.isEmpty() || !optionalApplicationUserRole.get().getIsActive()) {
            throw new InvalidCredentialException("Invalid Credentials");
        }
        optionalApplicationUserRole.get().setIsActive(false);
        applicationUserRoleRepository.save(optionalApplicationUserRole.get());
    }

    @Override
    public ApplicationUser getUserByEmail(String email) {
        return applicationUserRepository.findApplicationUserByEmail(email).orElseThrow(() -> new InvalidCredentialException("Invalid Credentials"));
    }

    @Override
    public List<ApplicationUser> getAllActiveUsersByRoleType(String roleType) {
        userProfileFactory.setUserRoleType(roleType);
        return applicationUserRoleService.getAllApplicationUsersByRoleType(userProfileFactory.getUserRoleType());
    }

    public Optional<ApplicationUserRole> getUserByRoleTypeAndEmail(String email, UserRoleType roleType) {
        Optional<UserRole> optionalUserRole = userRoleService.getUserRoleByName(String.valueOf(roleType));
        if (optionalUserRole.isEmpty()) {
            throw new UserRoleDoesNotExistException("Invalid User Role");
        }
        Optional<ApplicationUser> optionalApplicationUser = applicationUserRepository.findApplicationUserByEmail(email);
        if (optionalApplicationUser.isEmpty()) {
            throw new InvalidCredentialException("Invalid Credentials");
        }
        return applicationUserRoleRepository.findApplicationUserRolesByApplicationUserAndUserRole(optionalApplicationUser.get(),
                optionalUserRole.get());
    }

    public ApplicationUser getUserByIdAndRoleType(String id, String roleType) {
        userProfileFactory.setUserRoleType(roleType);
        UserProfileService userProfileService = userProfileServiceMap.get(userProfileFactory.getUserProfileServiceBeanName());
        return userProfileService.getUserProfileByUserId(id);
    }
}
