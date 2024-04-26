package com.example.taskflow.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CreateProjectRequest {
    private String title;
    private int projectManageId;
//    private Date startDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }

    public int getProjectManageId() {
        return projectManageId;
    }

    public void setProjectManageId(int projectManageId) {
        this.projectManageId = projectManageId;
    }
}
