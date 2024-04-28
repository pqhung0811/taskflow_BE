package com.example.taskflow.dtos;

import com.example.taskflow.entities.User;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CommentDto {
    private int id;
    private String content;
    private UserDto author;
    private LocalDateTime date;

    public void setupUser(int id, String name, String email) {
        this.author = new UserDto();
        this.author.setId(id);
        this.author.setEmail(email);
        this.author.setName(name);
    }
}
