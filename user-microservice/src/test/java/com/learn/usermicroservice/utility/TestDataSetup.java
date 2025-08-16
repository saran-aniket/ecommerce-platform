package com.learn.usermicroservice.utility;

import com.learn.usermicroservice.models.entities.ApplicationUser;
import com.learn.usermicroservice.models.entities.Customer;
import com.learn.usermicroservice.models.entities.UserRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataSetup {

    public static ApplicationUser getSingleApplicationUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setEmail("testEmail.com");
        applicationUser.setPassword("password");
        applicationUser.setUserRoles(getCustomerUserRole());
        applicationUser.setRegisteredOn(new Date());
        return applicationUser;

    }
//    public static List<ApplicationUser> getApplicationUserList(int listSize){
//        List<ApplicationUser> applicationUserList = new ArrayList<>();
//        int i = 1;
//        while(i<=listSize){
//            ApplicationUser applicationUser = new ApplicationUser();
//            applicationUser.setEmail("testEmail"+i+".com");
//            applicationUser.setPassword("password"+i);
//            applicationUser.setUserRoles(getCustomerUserRole());
//            applicationUser.setRegisteredOn(new Date());
//            applicationUserList.add(applicationUser);
//            i++;
//        }
//        return applicationUserList;
//    }

    public static List<UserRole> getCustomerUserRole() {
        List<UserRole> userRoleList = new ArrayList<>();
        UserRole userRole = new UserRole();
        userRole.setName("CUSTOMER");
        userRoleList.add(userRole);
        return userRoleList;
    }


    public static Customer getSingleCustomer() {
        ApplicationUser applicationUser = getSingleApplicationUser();

        Customer customer = new Customer();
        customer.setFirstName("firstNameTest");
        customer.setLastName("lastNameTest");
        customer.setPhoneNumber("1234567890");

        return customer;
    }
}
