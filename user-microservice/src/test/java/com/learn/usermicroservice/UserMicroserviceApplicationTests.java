package com.learn.usermicroservice;

import com.learn.usermicroservice.models.entities.UserRole;
import com.learn.usermicroservice.repositories.UserRoleRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("default")
@TestPropertySource(properties = {"jwt.secret=secret", "jwt.expiration=3600"})
class UserMicroserviceApplicationTests {


    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    void loadUserRole(){
        List<UserRole> userRole = new ArrayList<>(2);
        userRole.add(new UserRole());
        userRole.add(new UserRole());
        userRole.get(0).setName("ROLE_CUSTOMER");
        userRole.get(1).setName("ROLE_SELLER");
        userRoleRepository.saveAll(userRole);

    }
//
//    @Test
//    @Order(2)
//    void testJwt(){
//        UserRole userRole = userRoleRepository.findUserRoleByName("ROLE_CUSTOMER").get();
//        ApplicationUser applicationUser = new ApplicationUser();
//        applicationUser.setUsername("test@yopmail.com");
//        applicationUser.setUserRoles(List.of(userRole));
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", userRole.getName());
//        String token = jwtService.generateToken(claims, applicationUser);
//        System.out.println("Token Generated: "+token);
//        jwtService.validateToken(token, applicationUser);
//        System.out.println("Token validated: "+token);
//    }

}
