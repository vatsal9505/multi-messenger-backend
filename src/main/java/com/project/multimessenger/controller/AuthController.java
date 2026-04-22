package com.project.multimessenger.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.multimessenger.dto.LoginRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    String email = request.getEmail();
    String password = request.getPassword();

    if ("admin".equals(email) && "1234".equals(password)) {

        Map<String, String> response = new HashMap<>();
        response.put("token", "demo-token");
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    Map<String, String> error = new HashMap<>();
    error.put("message", "Invalid credentials");

    return ResponseEntity.status(401).body(error);
}
}