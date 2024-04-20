package com.example.taskflow.dtos;

import lombok.Data;

@Data
public class UserDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
