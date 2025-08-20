package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.AuthenticationDto;
import com.learn.usermicroservice.exceptions.InvalidCredentialException;
import com.learn.usermicroservice.exceptions.UnauthorizedException;
import com.learn.usermicroservice.factories.UserProfileFactory;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.ApplicationUserRole;
import com.learn.usermicroservice.models.enums.UserRoleType;
import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.repositories.ApplicationUserRoleRepository;
import com.learn.usermicroservice.services.TokenBlackListService;
import com.learn.usermicroservice.services.UserAuthService;
import com.learn.usermicroservice.services.UserRoleService;
import com.learn.usermicroservice.services.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtServiceImpl jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApplicationUserRepository applicationUserRepository;
    private final TokenBlackListService tokenBlackListService;
    private final UserProfileFactory userProfileFactory;
    private final UserRoleService userRoleService;
    private final ApplicationUserRoleRepository applicationUserRoleRepository;
    private final UserService userService;

    public UserAuthServiceImpl(JwtServiceImpl jwtService, BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationUserRepository applicationUserRepository, TokenBlackListService tokenBlackListService, UserProfileFactory userProfileFactory, UserRoleService userRoleService, ApplicationUserRoleRepository applicationUserRoleRepository, UserService userService) {
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.applicationUserRepository = applicationUserRepository;
        this.tokenBlackListService = tokenBlackListService;
        this.userProfileFactory = userProfileFactory;
        this.userRoleService = userRoleService;
        this.applicationUserRoleRepository = applicationUserRoleRepository;
        this.userService = userService;
    }

    @Override
    public Token login(String email, String password, String roleType) throws InvalidCredentialException {
        Optional<ApplicationUserRole> optionalApplicationUserRole = userService.getUserByRoleTypeAndEmail(email, UserRoleType.valueOf(roleType));

        if (optionalApplicationUserRole.isEmpty() || !optionalApplicationUserRole.get().getIsActive()) {
            throw new InvalidCredentialException("Invalid Credentials");
        }
        ApplicationUser applicationUser = optionalApplicationUserRole.get().getApplicationUser();
        if (!bCryptPasswordEncoder.matches(password, applicationUser.getPassword())) {
            throw new InvalidCredentialException("Wrong Password");
        }

        Token token = new Token();
        String compactTokenString = jwtService.generateToken(applicationUser.getClaims());
        token.setAccessToken(compactTokenString);
        token.setExpiresAt(jwtService.extractExpiration(compactTokenString).getTime());
        return token;
    }

    @Override
    public void logout(String token) {
        tokenBlackListService.addTokenToBlackList(token);
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    @Override
    public AuthenticationDto getAuthentication(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("No access token can be found");
        }
        String jwtToken = authorizationHeader.substring(7);
        if (tokenBlackListService.isTokenBlackListed(jwtToken)) {
            throw new AuthenticationCredentialsNotFoundException("Invalid Credentials");
        }
        jwtService.validateToken(jwtToken);
        Claims claims = jwtService.extractAllClaims(jwtToken);
        log.info("Token Claims: {}", claims.toString());
        return AuthenticationDto.from(claims);
    }
}
