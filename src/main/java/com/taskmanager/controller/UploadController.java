package com.taskmanager.controller;
import com.taskmanager.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
 private final FileUploadService fileUploadService;
 @PostMapping
 public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
 String fileName = fileUploadService.uploadFile(file);
 return Map.of(
 "message", "File uploaded successfully",
 "fileName", fileName
 );
 }
}