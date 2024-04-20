package com.example.taskflow.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CreateProjectRequest {
    private String name;
    private Date startDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
