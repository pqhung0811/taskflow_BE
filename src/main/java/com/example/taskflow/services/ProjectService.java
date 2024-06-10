package com.example.taskflow.services;

import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.Task;
import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.ProjectRepository;
import com.example.taskflow.reponsitories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

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
        projectRepository.deleteAllMemberProject(projectId);
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
    public Project removeMember(int userId, Project project) {
        List<User> users = project.getMembers();
        taskRepository.setResponsibleToNullByUserId(userId, project.getId());
        for (User user : users) {
            if (userId==user.getId()) {
                users.remove(user);
                break;
            }
        }
        projectRepository.save(project);
        return project;
    }
}
