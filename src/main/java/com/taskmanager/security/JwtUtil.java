package com.taskmanager.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
@Component
public class JwtUtil {
 private final String SECRET = "your_secret_key"; // move to application.properties
 public String generateToken(String userId) {
 return Jwts.builder()
 .setSubject(userId)
 .setIssuedAt(new Date())
 .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
 .signWith(SignatureAlgorithm.HS256, SECRET)
 .compact();
 }
}