package com.taskmanager.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {
 private final String SECRET = "your_secret_key"; // move to properties
 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
 throws IOException, ServletException {
 HttpServletRequest req = (HttpServletRequest) request;
 String header = req.getHeader("Authorization");
 if (header != null && header.startsWith("Bearer ")) {
 String token = header.substring(7);
 try {
 Claims claims = Jwts.parser()
 .setSigningKey(SECRET)
 .parseClaimsJws(token)
 .getBody();
 String userId = claims.getSubject();
 // Attach user info like req.user
 req.setAttribute("userId", userId);
 } catch (Exception e) {
 throw new ServletException("Invalid Token");
 }
 }
 chain.doFilter(request, response);
 }
}