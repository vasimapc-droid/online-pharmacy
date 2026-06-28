package com.pharmacy.service;

import com.pharmacy.model.User;
import com.pharmacy.model.User.Role;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> register(Map<String, String> request) {
        try {
            String fullName = request.get("fullName");
            String email = request.get("email");
            String password = request.get("password");
            String roleStr = request.get("role");
            String phone = request.getOrDefault("phone", "");
            String specialty = request.getOrDefault("specialty", null);
            String licenseNumber = request.getOrDefault("licenseNumber", null);
            String pharmacyName = request.getOrDefault("pharmacyName", null);
            String address = request.getOrDefault("address", null);

            if (fullName == null || fullName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Full name is required"));
            }
            if (email == null || !email.contains("@")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Valid email is required"));
            }
            if (password == null || password.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
            }
            if (roleStr == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Role is required"));
            }

            if (userRepository.findByEmail(email.trim().toLowerCase()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already registered"));
            }

            Role role;
            try {
                role = Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid role. Use PATIENT, DOCTOR, PHARMACY, or ADMIN"));
            }

            User user = new User();
            user.setFullName(fullName.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPassword(encoder.encode(password));
            user.setRole(role);
            user.setPhone(phone);
            user.setCreatedAt(LocalDateTime.now());

            if (role == Role.DOCTOR) {
                user.setSpecialty(specialty);
                user.setLicenseNumber(licenseNumber);
            } else if (role == Role.PHARMACY) {
                user.setPharmacyName(pharmacyName);
                user.setAddress(address);
            }

            User savedUser = userRepository.save(user);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", savedUser.getId());
            response.put("fullName", savedUser.getFullName());
            response.put("email", savedUser.getEmail());
            response.put("role", savedUser.getRole().toString());
            response.put("message", "Registration successful");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> login(String email, String password) {
        if (email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email and password are required"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email.trim().toLowerCase());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        User user = userOpt.get();

        if (!encoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().toString());
        response.put("phone", user.getPhone());
        response.put("message", "Login successful");

        if (user.getRole() == Role.DOCTOR) {
            response.put("specialty", user.getSpecialty());
        } else if (user.getRole() == Role.PHARMACY) {
            response.put("pharmacyName", user.getPharmacyName());
        }

        return ResponseEntity.ok(response);
    }
}