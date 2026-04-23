package com.taskmanager.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserWithTaskDTO {
 private String id;
 private String name;
 private String email;
 private String role;
 private long pendingTasks;
 private long inProgressTasks;
 private long completedTasks;
}
