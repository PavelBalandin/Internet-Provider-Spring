package com.provider.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String login;
    private String firstName;
    private String lastName;
    private String password;
}
