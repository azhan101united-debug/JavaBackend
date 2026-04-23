package com.taskmanager.dto;
import com.taskmanager.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
public class TaskResponseDTO {
 private List<Task> tasks;
 private Map<String, Long> statusSummary;
}
