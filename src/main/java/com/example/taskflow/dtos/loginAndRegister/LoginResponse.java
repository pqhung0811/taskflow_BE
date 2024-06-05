package com.example.taskflow.dtos.loginAndRegister;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String email;
    private String password;
    private int id;
    public LoginResponse(String accessToken, String email, String password, int id) {
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.id = id;
    }
}
