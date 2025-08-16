package com.learn.usermicroservice.services.implementation;

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
import com.learn.usermicroservice.services.UserProfileService;
import com.learn.usermicroservice.services.UserRoleService;
import com.learn.usermicroservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public UserServiceImpl(CustomerRepository customerRepository, ApplicationUserRepository applicationUserRepository, UserRoleService userRoleService, UserProfileFactory userProfileFactory, Map<String, UserProfileService> userProfileServiceMap) {
        this.applicationUserRepository = applicationUserRepository;
        this.userRoleService = userRoleService;
        this.userProfileFactory = userProfileFactory;
        this.userProfileServiceMap = userProfileServiceMap;
    }


    @Override
    public ApplicationUser createUser(UserSignupRequestDto userSignupRequestDto, String roleType) {
        userProfileFactory.setUserRoleType(roleType);
        UserRole userRole = new UserRole();
        userRole.setName(userProfileFactory.getUserRoleType().name());
        String userProfileServiceBeanName = userProfileFactory.getUserProfileServiceBeanName();
        UserProfileService userProfileService = userProfileServiceMap.get(userProfileServiceBeanName);
        UserSignupRequestDto convertedUserSignupRequestDto = userProfileFactory.getConvertedUserSignupRequestDto(userSignupRequestDto);

        //check if the Role Type exists
        try {
            userRole = userRoleService.getUserRoleByName(String.valueOf(userProfileFactory.getUserRoleType()));
        } catch (UserRoleDoesNotExistException e) {
            log.error("Role type {} does not exists", userRole.getName());
            userRoleService.createUserRole(userRole);
        }

        //Check if the user already exists
        Optional<ApplicationUser> optionalApplicationUser =
                applicationUserRepository.findApplicationUserByEmail(userSignupRequestDto.getEmail());
        if (optionalApplicationUser.isPresent()) {
            log.info("User with email {} already exists", userSignupRequestDto.getEmail());
            List<UserRole> existingUserRole = optionalApplicationUser.get().getUserRoles();
            if (existingUserRole.contains(userRole)) {
                log.info("User with email {} already has role {}", userSignupRequestDto.getEmail(), userRole.getName());
                throw new DuplicateEmailException("User with email " + userSignupRequestDto.getEmail() + " already exists");
            } else {
                log.info("User with email {} does not have role {}", userSignupRequestDto.getEmail(), userRole.getName());
                existingUserRole.add(userRole);
                optionalApplicationUser.get().setUserRoles(existingUserRole);
                applicationUserRepository.save(optionalApplicationUser.get());
                userProfileService.createUserProfile(userSignupRequestDto, optionalApplicationUser.get());
                return optionalApplicationUser.get();
            }
        } else {
            //create new user
            List<UserRole> userRoles = new ArrayList<>();
            userRoles.add(userRole);

            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setUserRoles(userRoles);

            if (userProfileService != null) {
                applicationUser = userProfileService.createUserProfile(convertedUserSignupRequestDto, applicationUser);
            }
            return applicationUser;
        }
    }

    @Override
    public ApplicationUser updateUser(UserUpdateRequestDto userUpdateRequestDto, String roleType) {
        ApplicationUser applicationUser = getUserByRoleTypeAndEmail(userUpdateRequestDto.getEmail(),
                UserRoleType.valueOf(roleType));
        userProfileFactory.setUserRoleType(roleType);
        String userProfileServiceBeanName = userProfileFactory.getUserProfileServiceBeanName();
        UserProfileService userProfileService = userProfileServiceMap.get(userProfileServiceBeanName);
        UserUpdateRequestDto convertedUserUpdateRequestDto = userProfileFactory.getConvertedUserUpdateRequestDto(userUpdateRequestDto);

        applicationUser = userProfileService.updateUserProfile(convertedUserUpdateRequestDto, applicationUser);

        return applicationUser;
    }

    @Override
    public void deleteUser(String email, String roleType) {
        ApplicationUser applicationUser = getUserByRoleTypeAndEmail(email, UserRoleType.valueOf(roleType));
        applicationUser.setActive(false);
        applicationUserRepository.save(applicationUser);
    }

    @Override
    public ApplicationUser getUserByEmail(String email) {
        return applicationUserRepository.findApplicationUserByEmail(email).orElseThrow(() -> new InvalidCredentialException("Invalid Credentials"));
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return applicationUserRepository.findAllByIsActiveTrue();
    }

    private ApplicationUser getUserByRoleTypeAndEmail(String email, UserRoleType roleType) {
        UserRole userRole = userRoleService.getUserRoleByName(String.valueOf(roleType));
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRole);
        Optional<ApplicationUser> optionalApplicationUser = applicationUserRepository.findApplicationUserByEmailAndUserRoles(email, userRoles);
        if (optionalApplicationUser.isEmpty()) {
            throw new InvalidCredentialException("Invalid Credentials");
        } else {
            return optionalApplicationUser.get();
        }
    }

}
