package com.example.taskflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskflowApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
