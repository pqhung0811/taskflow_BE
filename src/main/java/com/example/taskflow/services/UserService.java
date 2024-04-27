package com.example.taskflow.services;

import com.example.taskflow.dtos.UserDto;
import com.example.taskflow.entities.User;
import com.example.taskflow.reponsitories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(String email, String name, String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        return user;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(int id) {
        UserDetails user = userRepository.findUserDetailsById(id);
        return user;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean checkExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateNameById(String name, int userId) {
        userRepository.updateNameById(name, userId);
    }
}
