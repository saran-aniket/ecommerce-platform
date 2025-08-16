package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.exceptions.CustomerNotFoundException;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.repositories.CustomerRepository;
import com.learn.usermicroservice.services.UserProfileService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("customerUserProfileService")
public class CustomerUserProfileServiceImpl implements UserProfileService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerUserProfileServiceImpl(CustomerRepository customerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Customer createUserProfile(UserSignupRequestDto userSignupRequestDto, ApplicationUser applicationUser) {
        Customer customer = new Customer();
        customer.setFirstName(userSignupRequestDto.getFirstName());
        customer.setLastName(userSignupRequestDto.getLastName());
        customer.setPhoneNumber(userSignupRequestDto.getPhoneNumber());
        customer.setEmail(userSignupRequestDto.getEmail());
        customer.setPassword(bCryptPasswordEncoder.encode(userSignupRequestDto.getPassword()));
        customer.setActive(true);
        customer.setRegisteredOn(new java.util.Date());
        customer.setUserRoles(applicationUser.getUserRoles());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateUserProfile(UserUpdateRequestDto userUpdateRequestDto, ApplicationUser applicationUser) {
        Customer customer = getUserProfileByApplicationUser(applicationUser.getEmail());
        customer.setFirstName(userUpdateRequestDto.getFirstName());
        customer.setLastName(userUpdateRequestDto.getLastName());
        customer.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        return customerRepository.save(customer);
    }

    @Override
    public Customer getUserProfileByApplicationUser(String email) {
        Optional<Customer> customer = customerRepository.findCustomerByEmail(email);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found for email " + email);
        }
        return customer.get();
    }

    @Override
    public ApplicationUser getUserProfileByUserId(String id) {
        Optional<Customer> customer = customerRepository.getCustomersById(UUID.fromString(id));
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found for id " + id);
        }
        return customer.get();
    }
}
