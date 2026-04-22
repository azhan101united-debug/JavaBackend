package com.taskmanager.model;
import lombok.Data;
@Data
public class Todo {
 private String text;
 private boolean completed = false;
}
