package com.example.taskflow;

import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.UserRepository;
import com.example.taskflow.services.FileAttachmentService;
import com.example.taskflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TaskflowApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowApplication.class, args);
	}

	@Autowired
	FileAttachmentService fileAttachmentService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
	}
}
