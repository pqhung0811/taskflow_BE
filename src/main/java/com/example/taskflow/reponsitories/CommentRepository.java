package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
//    @Query("SELECT c FROM comment c WHERE c.parent.id = :parentId")
//    public List<Comment> findCommentByParent(int parentId);
}
