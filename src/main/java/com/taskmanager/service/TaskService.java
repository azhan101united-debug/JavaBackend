package com.taskmanager.service;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
@Service
@RequiredArgsConstructor
public class TaskService {
 private final TaskRepository taskRepository;
 public TaskResponseDTO getTasks(String userId, String role, String status) {
 Task.Status taskStatus = null;
 if (status != null) {
 taskStatus = Task.Status.valueOf(status.toUpperCase().replace(" ", "_"));
 }
 List<Task> tasks;
 if (role.equals("ADMIN")) {
 tasks = (taskStatus != null)
 ? taskRepository.findByStatus(taskStatus)
 : taskRepository.findAll();
 } else {
 tasks = (taskStatus != null)
 ? taskRepository.findByAssignedToContainsAndStatus(userId, taskStatus)
 : taskRepository.findByAssignedToContains(userId);
 }
 // Add completed checklist count
 tasks.forEach(task -> {
 long completed = task.getTodoChecklist() == null ? 0 :
 task.getTodoChecklist().stream().filter(t -> t.isCompleted()).count();
 task.setProgress((int) completed); // simplified
 });
 long all = role.equals("ADMIN")
 ? taskRepository.count()
 : taskRepository.countByAssignedToContains(userId);
 long pending = taskRepository.countByAssignedToContainsAndStatus(userId, Task.Status.PENDING);
 long inProgress = taskRepository.countByAssignedToContainsAndStatus(userId, Task.Status.IN_PROGRESS);
 long completed = taskRepository.countByAssignedToContainsAndStatus(userId, Task.Status.COMPLETED);
 Map<String, Long> summary = new HashMap<>();
 summary.put("all", all);
 summary.put("pendingTasks", pending);
 summary.put("inProgressTasks", inProgress);
 summary.put("completedTasks", completed);
 return new TaskResponseDTO(tasks, summary);
 }
 public Task getTaskById(String id) {
 return taskRepository.findById(id)
 .orElseThrow(() -> new RuntimeException("Task not found"));
 }
 public Task createTask(Task task, String userId) {
 task.setCreatedBy(userId);
 return taskRepository.save(task);
 }
 public Task updateTask(String id, Task updated) {
 Task task = getTaskById(id);
 if (updated.getTitle() != null) task.setTitle(updated.getTitle());
 if (updated.getDescription() != null) task.setDescription(updated.getDescription());
 if (updated.getPriority() != null) task.setPriority(updated.getPriority());
 if (updated.getDueDate() != null) task.setDueDate(updated.getDueDate());
 if (updated.getAssignedTo() != null) task.setAssignedTo(updated.getAssignedTo());
 if (updated.getAttachments() != null) task.setAttachments(updated.getAttachments());
 if (updated.getTodoChecklist() != null) task.setTodoChecklist(updated.getTodoChecklist());
 return taskRepository.save(task);
 }
 public void deleteTask(String id) {
 taskRepository.deleteById(id);
 }
 public Task updateStatus(String id, String status) {
 Task task = getTaskById(id);
 task.setStatus(Task.Status.valueOf(status.toUpperCase().replace(" ", "_")));
 if (task.getStatus() == Task.Status.COMPLETED) {
 task.getTodoChecklist().forEach(item -> item.setCompleted(true));
 task.setProgress(100);
 }
 return taskRepository.save(task);
 }
 public Task updateChecklist(String id, List<com.taskmanager.model.Todo> checklist) {
 Task task = getTaskById(id);
 task.setTodoChecklist(checklist);
 long completed = checklist.stream().filter(t -> t.isCompleted()).count();
 int total = checklist.size();
 int progress = total > 0 ? (int) ((completed * 100) / total) : 0;
 task.setProgress(progress);
 if (progress == 100) task.setStatus(Task.Status.COMPLETED);
 else if (progress > 0) task.setStatus(Task.Status.IN_PROGRESS);
 else task.setStatus(Task.Status.PENDING);
 return taskRepository.save(task);
 }
 public Map<String, Object> getDashboard() {
 long total = taskRepository.count();
 long pending = taskRepository.countByStatus(Task.Status.PENDING);
 long completed = taskRepository.countByStatus(Task.Status.COMPLETED);
 long overdue = taskRepository.countByStatusNotAndDueDateBefore(Task.Status.COMPLETED, Instant.now());
 Map<String, Object> result = new HashMap<>();
 result.put("totalTasks", total);
 result.put("pendingTasks", pending);
 result.put("completedTasks", completed);
 result.put("overdueTasks", overdue);
 return result;
 }
}
