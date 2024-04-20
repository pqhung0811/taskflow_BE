package com.example.taskflow.services;

import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void addMemberToProject(User user, Project project) {
        projectRepository.addMemberToProject(project.getId(), user.getId());
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
    public void createProject(String name, Date startDate) {
        Project project = new Project();
        project.setName(name);
        project.setStartDate(startDate);
        projectRepository.save(project);
    }
}
