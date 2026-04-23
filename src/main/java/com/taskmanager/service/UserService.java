package com.taskmanager.service;
import com.taskmanager.dto.UserWithTaskDTO;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserService {
 private final UserRepository userRepository;
 private final TaskRepository taskRepository;
 public List<UserWithTaskDTO> getAllUsersWithTaskCounts() {
 List<User> users = userRepository.findAll()
 .stream()
 .filter(user -> user.getRole() == User.Role.MEMBER)
 .collect(Collectors.toList());
 return users.stream().map(user -> {
 long pending = taskRepository.countByAssignedToContainsAndStatus(
 user.getId(), Task.Status.PENDING);
 long inProgress = taskRepository.countByAssignedToContainsAndStatus(
 user.getId(), Task.Status.IN_PROGRESS);
 long completed = taskRepository.countByAssignedToContainsAndStatus(
 user.getId(), Task.Status.COMPLETED);
 return new UserWithTaskDTO(
 user.getId(),
 user.getName(),
 user.getEmail(),
 user.getRole().name(),
 pending,
 inProgress,
 completed
 );
 }).collect(Collectors.toList());
 }
 public User getUserById(String id) {
 return userRepository.findById(id)
 .orElseThrow(() -> new RuntimeException("User not found"));
 }
}
