package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.repositories.ApplicationUserRepository;
import com.learn.usermicroservice.services.CustomUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailService {

    private final ApplicationUserRepository applicationUserRepository;

    public CustomUserDetailServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserRepository.findApplicationUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
