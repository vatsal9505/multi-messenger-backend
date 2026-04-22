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
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getEmail()) && "1234".equals(request.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("token", "demo-token");
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }
}