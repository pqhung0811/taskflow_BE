package com.example.taskflow.controllers;

import com.example.taskflow.entities.ImageData;
import com.example.taskflow.entities.User;
import com.example.taskflow.services.ImageDataService;
import com.example.taskflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/image/{userId}")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @PathVariable("userId") int userId) throws IOException {
        User user= userService.getUserById(userId);
        String response= imageDataService.uploadImage(file,user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/image/info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getImageInfoByUserId(@PathVariable("userId") int userId, HttpServletResponse response) {
        User user = userService.getUserById(userId);
        ImageData imageData = imageDataService.getImageByUser(user);

        response.setContentType("image/jpeg");
        response.setHeader("Content-Length", String.valueOf(imageData.getImageData().length));

        try {
            response.getOutputStream().write(imageData.getImageData());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
