package com.example.taskflow.dtos;

import lombok.Data;

@Data
public class JoinMemberRequest {
    private String email;
    private int id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
