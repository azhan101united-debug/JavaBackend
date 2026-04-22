package com.taskmanager.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
@Data
@Document(collection = "tasks")
public class Task {
 @Id
 private String id;
 private String title;
 private String description;
 private Priority priority = Priority.MEDIUM;
 private Status status = Status.PENDING;
 private Instant dueDate;
 private List<String> assignedTo; // User IDs
 private String createdBy; // User ID
 private List<String> attachments;
 private List<Todo> todoChecklist;
 private int progress = 0;
 private Instant createdAt;
 private Instant updatedAt;
 public enum Priority {
 LOW,
 MEDIUM,
 HIGH
 }
 public enum Status {
 PENDING,
 IN_PROGRESS,
 COMPLETED
 }
}