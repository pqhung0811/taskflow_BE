package com.example.taskflow.controllers;

import com.example.taskflow.dtos.*;
import com.example.taskflow.entities.User;
import com.example.taskflow.securities.JwtTokenProvider;
import com.example.taskflow.services.CustomUserDetails;
import com.example.taskflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        int id = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        return new LoginResponse(jwt, loginRequest.getEmail(), loginRequest.getPassword(), id);
    }

    @PostMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewUser(@RequestBody RegisterRequest registerRequest) {
        String email=registerRequest.getEmail();
        Map<String, String> error = new HashMap<>();
        error.put("error", "Email already exists");

        if (userService.checkExistEmail(email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        User user = userService.addUser(registerRequest.getEmail(), registerRequest.getName(),
                passwordEncoder.encode(registerRequest.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping(path="/updateNameOfUser", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> modifyUserName(@RequestBody RegisterRequest registerRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            if (userService.checkExistEmail(registerRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();
            userService.updateNameById(registerRequest.getName(), user.getId());

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @DeleteMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteUser(@PathVariable Integer id) {
        User user= userService.getUserById(id);
        userService.deleteUser(user);
    }
}
