package com.pharmacy.controller;

import com.pharmacy.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody Map<String, Object> request) {
        return prescriptionService.createPrescription(request);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientPrescriptions(@PathVariable Long patientId) {
        return prescriptionService.getPatientPrescriptions(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorPrescriptions(@PathVariable Long doctorId) {
        return prescriptionService.getDoctorPrescriptions(doctorId);
    }
}