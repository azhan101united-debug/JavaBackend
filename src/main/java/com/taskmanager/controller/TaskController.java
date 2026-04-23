package com.taskmanager.controller;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.model.Todo;
import com.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
 private final TaskService taskService;
 // Admin Dashboard
 @GetMapping("/dashboard-data")
 @PreAuthorize("hasRole('ADMIN')")
 public Map<String, Object> getDashboard() {
 return taskService.getDashboard();
 }
 // User Dashboard
 @GetMapping("/user-dashboard-data")
 public Map<String, Object> getUserDashboard(
 @RequestAttribute("userId") String userId
 ) {
 return taskService.getDashboard(); // (can customize per user later)
 }
 // Get all tasks
 @GetMapping
 public TaskResponseDTO getTasks(
 @RequestAttribute("userId") String userId,
 @RequestAttribute("role") String role,
 @RequestParam(required = false) String status
 ) {
 return taskService.getTasks(userId, role, status);
 }
 // Get task by ID
 @GetMapping("/{id}")
 public Task getTask(@PathVariable String id) {
 return taskService.getTaskById(id);
 }
 // Create task (Admin only)
 @PostMapping
 @PreAuthorize("hasRole('ADMIN')")
 public Task createTask(
 @RequestBody Task task,
 @RequestAttribute("userId") String userId
 ) {
 return taskService.createTask(task, userId);
 }
 // Update task
 @PutMapping("/{id}")
 public Task updateTask(@PathVariable String id, @RequestBody Task task) {
 return taskService.updateTask(id, task);
 }
 // Delete task (Admin only)
 @DeleteMapping("/{id}")
 @PreAuthorize("hasRole('ADMIN')")
 public String deleteTask(@PathVariable String id) {
 taskService.deleteTask(id);
 return "Task deleted successfully";
 }
 // Update task status
 @PutMapping("/{id}/status")
 public Task updateStatus(
 @PathVariable String id,
 @RequestParam String status
 ) {
 return taskService.updateStatus(id, status);
 }
 // Update checklist
 @PutMapping("/{id}/todo")
 public Task updateChecklist(
 @PathVariable String id,
 @RequestBody List<Todo> checklist
 ) {
 return taskService.updateChecklist(id, checklist);
 }
}
