package com.learn.usermicroservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"jwt.secret=secret", "jwt.expiration=3600"})
class UserMicroserviceApplicationTests {

}
