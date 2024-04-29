package com.example.taskflow.controllers;

import com.example.taskflow.entities.ImageData;
import com.example.taskflow.entities.User;
import com.example.taskflow.services.CustomUserDetails;
import com.example.taskflow.services.ImageDataService;
import com.example.taskflow.services.UserService;
import com.example.taskflow.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class AvatarController {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageDataService imageDataService;

    @PostMapping(path = "/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String response = imageDataService.uploadImage(file, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }
    }

    @GetMapping(path = "/image/info/{userId}")
    public void getImageInfoByUserId(@PathVariable("userId") int userId, HttpServletResponse response) {
        User user = userService.getUserById(userId);
        ImageData imageData = imageDataService.getImageByUser(user);
        byte[] image = imageData.getImageData();

        response.setContentType("image/jpeg");
        response.setHeader("Content-Length", String.valueOf(imageData.getImageData().length));
        try {
            response.getOutputStream().write(image);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
