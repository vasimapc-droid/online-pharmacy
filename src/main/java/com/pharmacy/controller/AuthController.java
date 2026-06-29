package com.pharmacy.controller;

import com.pharmacy.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        return authService.login(request.get("email"), request.get("password"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        return authService.forgotPassword(request.get("email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        return authService.resetPassword(request.get("token"), request.get("newPassword"));
    }
}