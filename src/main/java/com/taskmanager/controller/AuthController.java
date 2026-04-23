package com.taskmanager.controller;
import com.taskmanager.dto.*;
import com.taskmanager.model.User;
import com.taskmanager.service.AuthService;
import com.taskmanager.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
 private final AuthService authService;
 private final FileUploadService fileUploadService;
 // Register User
 @PostMapping("/register")
 public AuthResponse register(@RequestBody RegisterRequest request) {
 return authService.register(request);
 }
 // Login User
 @PostMapping("/login")
 public AuthResponse login(@RequestBody LoginRequest request) {
 return authService.login(request);
 }
 // Get Profile (Protected)
 @GetMapping("/profile")
 public User getProfile(@RequestAttribute("userId") String userId) {
 return authService.getProfile(userId);
 }
 // Update Profile (Protected)
 @PutMapping("/profile")
 public AuthResponse updateProfile(
 @RequestAttribute("userId") String userId,
 @RequestBody RegisterRequest request
 ) {
 return authService.updateProfile(userId, request);
 }
 // Upload Profile Image
 @PostMapping("/upload-image")
 public Map<String, String> uploadImage(
 @RequestParam("image") MultipartFile file,
 HttpServletRequest request
 ) throws Exception {
 if (file.isEmpty()) {
 throw new RuntimeException("No file uploaded");
 }
 String fileName = fileUploadService.uploadFile(file);
 String imageUrl = request.getScheme() + "://" +
 request.getServerName() + ":" +
 request.getServerPort() +
 "/uploads/" + fileName;
 return Map.of("imageUrl", imageUrl);
 }
}