package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.AuthenticationDto;
import com.learn.usermicroservice.exceptions.*;
import com.learn.usermicroservice.models.Token;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.models.enums.TokenClaims;
import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.repositories.CustomerRepository;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import com.learn.usermicroservice.services.CustomerService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlackListServiceImpl tokenBlackListService;

    public CustomerServiceImpl(CustomerRepository customerRepository, ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRoleRepository userRoleRepository, JwtServiceImpl jwtService, AuthenticationManager authenticationManager, TokenBlackListServiceImpl tokenBlackListService) {
        this.customerRepository = customerRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenBlackListService = tokenBlackListService;
    }


    @Override
    public Customer createCustomer(String firstName, String lastName, String email, String password,
                                   String phoneNumber){
        Optional<UserRole> userRoleOptional = userRoleRepository.findUserRoleByName("ROLE_CUSTOMER");
        List<UserRole> userRoles = new ArrayList<>();
        if(userRoleOptional.isEmpty()){
            UserRole userRole = new UserRole();
            userRole.setName("ROLE_CUSTOMER");
            userRoleRepository.save(userRole);
            userRoles.add(userRole);
        }else{
            userRoles.add(userRoleOptional.get());
        }
        Optional<ApplicationUser> optionalApplicationUser =
                applicationUserRepository.findApplicationUserByEmailAndUserRoles(email, userRoles);
        if (optionalApplicationUser.isPresent()) {
            throw new DuplicateEmailException("User with email "+email+" already exists");
        }

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setEmail(email);
        applicationUser.setPassword(bCryptPasswordEncoder.encode(password));
        applicationUser.setActive(true);
        applicationUser.setDeleted(false);
        applicationUser.setUsername(email);
        applicationUser.setRegisteredOn(new Date());
        applicationUser.setUserRoles(userRoles);
        applicationUserRepository.save(applicationUser);

        Customer customer = new Customer();
        customer.setApplicationUser(applicationUser);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String firstName, String lastName, String email, String phoneNumber) {
        ApplicationUser applicationUser = getUserByRoleTypeAndEmail(email);
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByApplicationUser(applicationUser);
        if(optionalCustomer.isPresent()){
            applicationUser.setUsername(email);
            applicationUserRepository.save(applicationUser);
            Customer customer = optionalCustomer.get();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPhoneNumber(phoneNumber);
            return customerRepository.save(customer);
        }
        else{
            throw new InvalidCredentialException("Invalid Credentials");
        }
    }



    @Override
    public void deleteCustomer(String email) {

    }

    @Override
    public Optional<Customer> getCustomerByEmail(String email) throws InvalidCredentialException {
        return Optional.empty();
    }

    @Override
    public Token login(String email, String password) throws InvalidCredentialException {
        Optional<ApplicationUser> optionalApplicationUser = applicationUserRepository.findApplicationUserByEmail(email);
        if(optionalApplicationUser.isEmpty()){
            throw new InvalidCredentialException("Invalid Credentials");
        }
        ApplicationUser applicationUser = optionalApplicationUser.get();
        if(!bCryptPasswordEncoder.matches(password, applicationUser.getPassword())){
            throw new InvalidCredentialException("Wrong Password");
        }

        Optional<Customer> optionalCustomer = customerRepository.findCustomerByApplicationUser(applicationUser);
        if(optionalCustomer.isEmpty()){
            throw new InvalidCredentialException("Customer does not exists");
        }

        Token token = new Token();
        String compactTokenString = jwtService.generateToken(optionalCustomer.get().getClaims());
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
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            throw new UnauthorizedException("No access token can be found");
        }
        String jwtToken = authorizationHeader.substring(7);
        if(tokenBlackListService.isTokenBlackListed(jwtToken)){
            throw new AuthenticationCredentialsNotFoundException("Invalid Credentials");
        }
        jwtService.validateToken(jwtToken);
        Claims claims = jwtService.extractAllClaims(jwtToken);
        return AuthenticationDto.from(claims);
    }

    private ApplicationUser getUserByRoleTypeAndEmail(String email) {
        Optional<UserRole> userRoleOptional = userRoleRepository.findUserRoleByName("ROLE_CUSTOMER");
        if(userRoleOptional.isEmpty()){
            throw new UserRoleDoesNotExistException("User role does not exists");
        }
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(userRoleOptional.get());
        Optional<ApplicationUser> optionalApplicationUser = applicationUserRepository.findApplicationUserByEmailAndUserRoles(email, userRoles);
        if(optionalApplicationUser.isEmpty()){
            throw new InvalidCredentialException("Invalid Credentials");
        }else{
            return optionalApplicationUser.get();
        }
    }

}
