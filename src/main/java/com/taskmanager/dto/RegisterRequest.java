package com.taskmanager.dto;
import lombok.Data;
@Data
public class RegisterRequest {
 private String name;
 private String email;
 private String password;
 private String profileImageUrl;
 private String adminInviteToken;
}