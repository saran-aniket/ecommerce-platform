package com.learn.usermicroservice;

import com.learn.usermicroservice.repositories.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource(properties = {"jwt.secret=secret", "jwt.expiration=3600"})
class UserMicroserviceApplicationTests {


    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void contextLoads() {
    }

//    @Test
//    @Order(1)
//    void loadUserRole(){
//        List<UserRole> userRole = new ArrayList<>(2);
//        userRole.add(new UserRole());
//        userRole.add(new UserRole());
//        userRole.get(0).setName("CUSTOMER");
//        userRole.get(1).setName("SELLER");
//        userRoleRepository.saveAll(userRole);
//
//    }
//
//    @Test
//    @Order(2)
//    void testJwt(){
//        UserRole userRole = userRoleRepository.findUserRoleByName("CUSTOMER").get();
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
