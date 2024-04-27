package com.example.taskflow.services;

import com.example.taskflow.entities.Comment;
import com.example.taskflow.entities.Task;
import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(User user, String text, Task task) {
        Comment comment = new Comment();
        comment.setContent(text);
        comment.setUser(user);
        comment.setTask(task);
        commentRepository.save(comment);
        return comment;
    }
}
