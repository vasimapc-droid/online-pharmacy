package com.pharmacy.controller;

import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalPatients", userRepository.findByRole(User.Role.PATIENT).size());
        stats.put("totalDoctors", userRepository.findByRole(User.Role.DOCTOR).size());
        stats.put("totalPharmacies", userRepository.findByRole(User.Role.PHARMACY).size());
        stats.put("totalUsers", userRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        List<User> patients = userRepository.findByRole(User.Role.PATIENT);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User p : patients) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("fullName", p.getFullName());
            map.put("email", p.getEmail());
            map.put("phone", p.getPhone());
            map.put("createdAt", p.getCreatedAt() != null ? p.getCreatedAt().toString() : "N/A");
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getAllDoctors() {
        List<User> doctors = userRepository.findByRole(User.Role.DOCTOR);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User d : doctors) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", d.getId());
            map.put("fullName", d.getFullName());
            map.put("email", d.getEmail());
            map.put("specialty", d.getSpecialty());
            map.put("licenseNumber", d.getLicenseNumber());
            map.put("phone", d.getPhone());
            map.put("createdAt", d.getCreatedAt() != null ? d.getCreatedAt().toString() : "N/A");
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pharmacies")
    public ResponseEntity<?> getAllPharmacies() {
        List<User> pharmacies = userRepository.findByRole(User.Role.PHARMACY);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User p : pharmacies) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("pharmacyName", p.getPharmacyName());
            map.put("email", p.getEmail());
            map.put("phone", p.getPhone());
            map.put("address", p.getAddress());
            map.put("createdAt", p.getCreatedAt() != null ? p.getCreatedAt().toString() : "N/A");
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
    }
}