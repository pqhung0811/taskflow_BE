package com.example.taskflow.controllers;

import com.example.taskflow.dtos.*;
import com.example.taskflow.dtos.loginAndRegister.*;
import com.example.taskflow.entities.ImageData;
import com.example.taskflow.entities.Notifications;
import com.example.taskflow.entities.User;
import com.example.taskflow.securities.JwtTokenProvider;
import com.example.taskflow.services.CustomUserDetails;
import com.example.taskflow.services.ImageDataService;
import com.example.taskflow.services.NotificationsService;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private ImageDataService imageDataService;

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

    @GetMapping(path = "/user/notify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();
            List<Notifications> notifications = notificationsService.getNotificationsByUserId(user.getId());
            List<NotificationsDto> notificationsDtos = new ArrayList<>();
            for (Notifications notifications1 : notifications) {
                NotificationsDto notificationsDto = new NotificationsDto(notifications1);
                notificationsDtos.add(notificationsDto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(notificationsDtos);
        }
    }

    @GetMapping(path = "/user/noNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNumberOfNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();
            List<Notifications> notifications = notificationsService.getNotificationsByUserId(user.getId());
            int noNotify = 0;
            for (Notifications n : notifications) {
                if (!n.isRead()) {
                    noNotify++;
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(noNotify);
        }
    }

    @PatchMapping(path = "/user/notifyState", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStateNotify() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();
            notificationsService.markAllNotificationsAsReadByUserId(user.getId());
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
    }

    @DeleteMapping(path = "/user/notify/{noticeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteNotify(@PathVariable int noticeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            notificationsService.deleteNotification(noticeId);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
    }

    @PatchMapping("/changepassword")
    public ResponseEntity<?> deleteNotify(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not same password");
        }
        userService.updatePassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()), user);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @GetMapping("/login/oauth2/code/google")
    public LoginResponse googleLogin(OAuth2AuthenticationToken token) {
        // Lấy thông tin người dùng từ token OAuth2
        UserDetails userDetails = (UserDetails) token.getPrincipal();
        String email = userDetails.getUsername();

        // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
        UserDetails existingUser = userService.loadUserByUsername(email);

        if (existingUser == null) {
            // Nếu email chưa tồn tại, tạo tài khoản mới cho người dùng
            User user = userService.addUser(email, userDetails.getUsername(), "");
            existingUser = new CustomUserDetails(user);
        }

        // Tạo JWT token
        String jwt = tokenProvider.generateToken((CustomUserDetails) existingUser);
        int id = ((CustomUserDetails) existingUser).getUser().getId();

        // Trả về token cho người dùng
        return new LoginResponse(jwt, email, "", id);
    }

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
