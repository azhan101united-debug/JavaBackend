package com.taskmanager.controller;
import com.taskmanager.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
 private final ReportService reportService;
 // Export all tasks (Admin only)
 @GetMapping("/export/tasks")
 @PreAuthorize("hasRole('ADMIN')")
 public ResponseEntity<byte[]> exportTasks() throws Exception {
 byte[] data = reportService.exportTasksReport();
 return ResponseEntity.ok()
 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks_report.xlsx")
 .contentType(MediaType.APPLICATION_OCTET_STREAM)
 .body(data);
 }
 // Export user report (Admin only)
 @GetMapping("/export/users")
 @PreAuthorize("hasRole('ADMIN')")
 public ResponseEntity<byte[]> exportUsers() throws Exception {
 byte[] data = reportService.exportUsersReport();
 return ResponseEntity.ok()
 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_report.xlsx")
 .contentType(MediaType.APPLICATION_OCTET_STREAM)
 .body(data);
 }
}