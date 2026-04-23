package com.taskmanager.controller;
import com.taskmanager.dto.UserWithTaskDTO;
import com.taskmanager.model.User;
import com.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
 private final UserService userService;
 // Get all users (Admin only)
 @GetMapping
 @PreAuthorize("hasRole('ADMIN')")
 public List<UserWithTaskDTO> getUsers() {
 return userService.getAllUsersWithTaskCounts();
 }
 // Get user by ID (Authenticated)
 @GetMapping("/{id}")
 public User getUserById(@PathVariable String id) {
 return userService.getUserById(id);
 }
}