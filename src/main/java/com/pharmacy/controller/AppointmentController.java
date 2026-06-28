package com.pharmacy.controller;

import com.pharmacy.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> request) {
        return appointmentService.bookAppointment(request);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientAppointments(@PathVariable Long patientId) {
        return appointmentService.getPatientAppointments(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.getDoctorAppointments(doctorId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return appointmentService.updateStatus(id, request.get("status"));
    }
}