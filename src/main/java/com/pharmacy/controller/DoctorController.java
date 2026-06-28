package com.pharmacy.controller;

import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        List<User> doctors = userRepository.findByRole(User.Role.DOCTOR);
        List<Map<String, Object>> result = new ArrayList<>();

        for (User d : doctors) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", d.getId());
            map.put("fullName", d.getFullName());
            map.put("specialty", d.getSpecialty());
            map.put("email", d.getEmail());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}
