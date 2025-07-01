package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserRequestDTO implements Serializable {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String auth0Id;
} 