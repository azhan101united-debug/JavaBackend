package com.taskmanager.service;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.util.*;
@Service
@RequiredArgsConstructor
public class ReportService {
 private final TaskRepository taskRepository;
 private final UserRepository userRepository;
 public byte[] exportTasksReport() throws Exception {
 List<Task> tasks = taskRepository.findAll();
 Workbook workbook = new XSSFWorkbook();
 Sheet sheet = workbook.createSheet("Task Report");
 Row header = sheet.createRow(0);
 String[] columns = {
 "Task ID", "Title", "Description", "Priority",
 "Status", "Due Date", "Assigned To"
 };
 for (int i = 0; i < columns.length; i++) {
 header.createCell(i).setCellValue(columns[i]);
 }
 int rowIdx = 1;
 for (Task task : tasks) {
 Row row = sheet.createRow(rowIdx++);
 String assignedUsers = (task.getAssignedTo() != null)
 ? String.join(", ", task.getAssignedTo())
 : "Unassigned";
 row.createCell(0).setCellValue(task.getId());
 row.createCell(1).setCellValue(task.getTitle());
 row.createCell(2).setCellValue(task.getDescription());
 row.createCell(3).setCellValue(task.getPriority().name());
 row.createCell(4).setCellValue(task.getStatus().name());
 String dueDate = task.getDueDate() != null
 ? task.getDueDate().atZone(ZoneId.systemDefault()).toLocalDate().toString()
 : "";
 row.createCell(5).setCellValue(dueDate);
 row.createCell(6).setCellValue(assignedUsers);
 }
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 workbook.write(out);
 workbook.close();
 return out.toByteArray();
 }
 public byte[] exportUsersReport() throws Exception {
 List<User> users = userRepository.findAll();
 List<Task> tasks = taskRepository.findAll();
 Map<String, Map<String, Object>> userMap = new HashMap<>();
 for (User user : users) {
 Map<String, Object> data = new HashMap<>();
 data.put("name", user.getName());
 data.put("email", user.getEmail());
 data.put("taskCount", 0);
 data.put("pending", 0);
 data.put("inProgress", 0);
 data.put("completed", 0);
 userMap.put(user.getId(), data);
 }
 for (Task task : tasks) {
 if (task.getAssignedTo() != null) {
 for (String userId : task.getAssignedTo()) {
 Map<String, Object> user = userMap.get(userId);
 if (user != null) {
 user.put("taskCount", (int) user.get("taskCount") + 1);
 switch (task.getStatus()) {
 case PENDING -> user.put("pending", (int) user.get("pending") + 1);
 case IN_PROGRESS -> user.put("inProgress", (int) user.get("inProgress") + 1);
 case COMPLETED -> user.put("completed", (int) user.get("completed") + 1);
 }
 }
 }
 }
 }
 Workbook workbook = new XSSFWorkbook();
 Sheet sheet = workbook.createSheet("User Report");
 Row header = sheet.createRow(0);
 String[] columns = {
 "Name", "Email", "Total Tasks",
 "Pending", "In Progress", "Completed"
 };
 for (int i = 0; i < columns.length; i++) {
 header.createCell(i).setCellValue(columns[i]);
 }
 int rowIdx = 1;
 for (Map<String, Object> user : userMap.values()) {
 Row row = sheet.createRow(rowIdx++);
 row.createCell(0).setCellValue((String) user.get("name"));
 row.createCell(1).setCellValue((String) user.get("email"));
 row.createCell(2).setCellValue((Integer) user.get("taskCount"));
 row.createCell(3).setCellValue((Integer) user.get("pending"));
 row.createCell(4).setCellValue((Integer) user.get("inProgress"));
 row.createCell(5).setCellValue((Integer) user.get("completed"));
 }
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 workbook.write(out);
 workbook.close();
 return out.toByteArray();
 }
}