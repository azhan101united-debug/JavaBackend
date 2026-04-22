package com.taskmanager.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
@Data
@Document(collection = "users")
public class User {
 @Id
 private String id;
 private String name;
 private String email;
 private String password;
 private String profileImageUrl;
 private Role role = Role.MEMBER;
 private Instant createdAt;
 private Instant updatedAt;
 public enum Role {
 ADMIN,
 MEMBER
 }
}