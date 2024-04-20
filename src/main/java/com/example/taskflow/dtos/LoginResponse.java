package com.example.taskflow.dtos;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String email;
    private String password;
    public LoginResponse(String accessToken, String email, String password) {
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
    }
}
