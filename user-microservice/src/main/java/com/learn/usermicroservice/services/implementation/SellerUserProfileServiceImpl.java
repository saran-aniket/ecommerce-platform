package com.learn.usermicroservice.services.implementation;

import com.learn.usermicroservice.dtos.UserSignupRequestDto;
import com.learn.usermicroservice.dtos.UserUpdateRequestDto;
import com.learn.usermicroservice.exceptions.SellerNotFoundException;
import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Seller;
import com.learn.usermicroservice.repositories.SellerRepository;
import com.learn.usermicroservice.services.UserProfileService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("sellerUserProfileService")
public class SellerUserProfileServiceImpl implements UserProfileService {

    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SellerUserProfileServiceImpl(SellerRepository sellerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.sellerRepository = sellerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Seller createUserProfile(UserSignupRequestDto userSignupRequestDto, ApplicationUser applicationUser) {
        Seller seller = new Seller();
        seller.setFirstName(userSignupRequestDto.getFirstName());
        seller.setLastName(userSignupRequestDto.getLastName());
        seller.setPhoneNumber(userSignupRequestDto.getPhoneNumber());
        seller.setEmail(userSignupRequestDto.getEmail());
        seller.setPassword(bCryptPasswordEncoder.encode(userSignupRequestDto.getPassword()));
        seller.setActive(true);
        seller.setRegisteredOn(new java.util.Date());
        seller.setUserRoles(applicationUser.getUserRoles());
        seller.setCompanyName(userSignupRequestDto.getCompanyName());
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateUserProfile(UserUpdateRequestDto userUpdateRequestDto, ApplicationUser applicationUser) {
        Seller seller = getUserProfileByApplicationUser(applicationUser.getEmail());
        seller.setFirstName(userUpdateRequestDto.getFirstName());
        seller.setLastName(userUpdateRequestDto.getLastName());
        seller.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        seller.setCompanyName(userUpdateRequestDto.getCompanyName());
        return sellerRepository.save(seller);
    }

    @Override
    public Seller getUserProfileByApplicationUser(String email) {
        Optional<Seller> optionalSeller = sellerRepository.findSellerByEmail(email);
        if (optionalSeller.isEmpty()) {
            throw new SellerNotFoundException("Seller not found for email");
        }
        return optionalSeller.get();
    }

    @Override
    public ApplicationUser getUserProfileByUserId(String id) {
        Optional<Seller> optionalSeller = sellerRepository.getSellerById(UUID.fromString(id));
        if (optionalSeller.isEmpty()) {
            throw new SellerNotFoundException("Seller not found for id " + id);
        }
        return optionalSeller.get();
    }
}
