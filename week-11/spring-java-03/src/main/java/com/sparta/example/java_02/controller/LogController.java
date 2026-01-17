package com.sparta.example.java_02.controller;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/logs-test")
public class LogController {

  @GetMapping
  public ResponseEntity<Map<String, String>> home() {
    log.info("Home endpoint was accessed");
    return ResponseEntity.ok(Map.of("message", "Welcome to Spring Boot logging with ELK Stack"));
  }

  @GetMapping("/error")
  public ResponseEntity<Map<String, String>> error() {
    try {
      int result = 1 / 0;
    } catch (Exception e) {
      log.error("ArithmeticException occurred", e);
    }
    return ResponseEntity.ok(Map.of("message", "Error logged"));
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<Map<String, Object>> getUser(@PathVariable int userId) {
    log.error("User {} accessed their profile", userId);
    return ResponseEntity.ok(Map.of("user_id", userId, "status", "success"));
  }
}