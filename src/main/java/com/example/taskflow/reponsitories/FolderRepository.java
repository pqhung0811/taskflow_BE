package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.Folder;
import com.example.taskflow.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository  extends JpaRepository<Folder, Integer> {
    List<Folder> findByProject(Project project);
}
