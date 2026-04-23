package com.taskmanager.service;
import com.taskmanager.dto.*;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {
 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder;
 private final JwtUtil jwtUtil;
 private final String ADMIN_INVITE_TOKEN = "admin_secret"; // move to properties
 public AuthResponse register(RegisterRequest request) {
 if (userRepository.findByEmail(request.getEmail()).isPresent()) {
 throw new RuntimeException("User already exists");
 }
 User.Role role = User.Role.MEMBER;
 if (request.getAdminInviteToken() != null &&
 request.getAdminInviteToken().equals(ADMIN_INVITE_TOKEN)) {
 role = User.Role.ADMIN;
 }
 User user = new User();
 user.setName(request.getName());
 user.setEmail(request.getEmail());
 user.setPassword(passwordEncoder.encode(request.getPassword()));
 user.setProfileImageUrl(request.getProfileImageUrl());
 user.setRole(role);
 User savedUser = userRepository.save(user);
 String token = jwtUtil.generateToken(savedUser.getId());
 return new AuthResponse(
 savedUser.getId(),
 savedUser.getName(),
 savedUser.getEmail(),
 savedUser.getRole().name(),
 savedUser.getProfileImageUrl(),
 token
 );
 }
 public AuthResponse login(LoginRequest request) {
 User user = userRepository.findByEmail(request.getEmail())
 .orElseThrow(() -> new RuntimeException("Invalid email or password"));
 if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
 throw new RuntimeException("Invalid email or password");
 }
 String token = jwtUtil.generateToken(user.getId());
 return new AuthResponse(
 user.getId(),
 user.getName(),
 user.getEmail(),
 user.getRole().name(),
 user.getProfileImageUrl(),
 token
 );
 }
 public User getProfile(String userId) {
 return userRepository.findById(userId)
 .orElseThrow(() -> new RuntimeException("User not found"));
 }
 public AuthResponse updateProfile(String userId, RegisterRequest request) {
 User user = userRepository.findById(userId)
 .orElseThrow(() -> new RuntimeException("User not found"));
 user.setName(request.getName() != null ? request.getName() : user.getName());
 user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
 if (request.getPassword() != null) {
 user.setPassword(passwordEncoder.encode(request.getPassword()));
 }
 User updatedUser = userRepository.save(user);
 String token = jwtUtil.generateToken(updatedUser.getId());
 return new AuthResponse(
 updatedUser.getId(),
 updatedUser.getName(),
 updatedUser.getEmail(),
 updatedUser.getRole().name(),
 updatedUser.getProfileImageUrl(),
 token
 );
 }
}
