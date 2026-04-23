package com.taskmanager.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
@Service
public class FileUploadService {
 private final String UPLOAD_DIR = "uploads/";
 private final List<String> allowedTypes = List.of(
 "image/jpeg",
 "image/png",
 "image/jpg"
 );
 public String uploadFile(MultipartFile file) throws IOException {
 // Validate file type (equivalent to multer fileFilter)
 if (!allowedTypes.contains(file.getContentType())) {
 throw new RuntimeException("Only .jpeg, .jpg and .png formats are allowed");
 }
 // Create folder if not exists
 File dir = new File(UPLOAD_DIR);
 if (!dir.exists()) {
 dir.mkdirs();
 }
 // Generate filename (same as multer)
 String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
 File destination = new File(UPLOAD_DIR + fileName);
 file.transferTo(destination);
 return fileName; // or return full path
 }
}