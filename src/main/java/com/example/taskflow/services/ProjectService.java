package com.example.taskflow.services;

import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjectsByUserId(int userId) {
        return projectRepository.getProjectByUserId(userId);
    }
    public List<User> getUserByProjectId(int projectId) {
        return projectRepository.getUserByProject(projectId);
    }
    public Optional<Project> getProjectById(int projectId) {
        return projectRepository.findById(projectId);
    }
    public Project addMemberToProject(User user, Project project) {
        projectRepository.addMemberToProject(project.getId(), user.getId());
        project.addMember(user);
        return project;
//        List<User> members = project.getMembers();
//        if (members == null) {
//            members = new ArrayList<>();
//            members.add(user);
//            project.setMembers(members);
//        }
//        else {
//        }
    }
    public boolean checkMemberInProject(User user, Project project) {
        List<User> usersInProject = project.getMembers();
        for (User user1 : usersInProject) {
            if (user.getId() == user1.getId()) {
                return false;
            }
        }
        return true;
    }
    public void deleteProject(int projectId) {
        projectRepository.deleteById(projectId);
    }
    public Project createProject(String name, LocalDateTime startDate, User user) {
        Project project = new Project();
        project.setName(name);
        project.setStartDate(startDate);
        project.setProjectManager(user);
        projectRepository.save(project);
        addMemberToProject(user, project);
        return project;
    }
}
