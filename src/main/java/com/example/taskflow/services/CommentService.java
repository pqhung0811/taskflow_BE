package com.example.taskflow.services;

import com.example.taskflow.entities.Comment;
import com.example.taskflow.entities.Task;
import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(User user, String text, Task task) {
        Comment comment = new Comment();
        comment.setContent(text);
        comment.setUser(user);
        comment.setTask(task);
        comment.setDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment;
    }

    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    public Comment updateCommentContent(int id, String newContent) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(newContent);
            return commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment not found with id " + id);
        }
    }
}
