package com.example.taskflow.dtos;

import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDto {
    private int id;
    private String name;
    private LocalDateTime startDate;
    private UserDto projectManager;
    private List<UserDto> members;

    public ProjectDto() {
    }

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.startDate = project.getStartDate();
        UserDto userDto = new UserDto();
        userDto.setId(project.getProjectManager().getId());
        userDto.setEmail(project.getProjectManager().getEmail());
        userDto.setName(project.getProjectManager().getName());
        this.projectManager = userDto;
        List<UserDto> members1 = new ArrayList<>();
        List<User> users = project.getMembers();
        for(User u : users) {
            UserDto userDto1 = new UserDto(u.getId(), u.getName(), u.getEmail());
            members1.add(userDto1);
        }
        this.members = members1;
    }
}
