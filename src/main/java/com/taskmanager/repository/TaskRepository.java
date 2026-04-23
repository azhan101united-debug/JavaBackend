package com.taskmanager.repository;
import com.taskmanager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.Instant;
import java.util.List;
public interface TaskRepository extends MongoRepository<Task, String> {
 List<Task> findByAssignedToContains(String userId);
 List<Task> findByStatus(Task.Status status);
 List<Task> findByAssignedToContainsAndStatus(String userId, Task.Status status);
 long countByAssignedToContains(String userId);
 long countByAssignedToContainsAndStatus(String userId, Task.Status status);
 long countByStatus(Task.Status status);
 long countByStatusNotAndDueDateBefore(Task.Status status, Instant date);
}
