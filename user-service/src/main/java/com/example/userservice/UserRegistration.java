package com.example.userservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistration {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobilePhone;
}