package com.pharmacy.service;

import com.pharmacy.model.Appointment;
import com.pharmacy.model.Prescription;
import com.pharmacy.model.User;
import com.pharmacy.repository.AppointmentRepository;
import com.pharmacy.repository.PrescriptionRepository;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createPrescription(Map<String, Object> request) {
        try {
            Long doctorId = Long.valueOf(request.get("doctorId").toString());
            Long patientId = Long.valueOf(request.get("patientId").toString());
            String diagnosis = request.getOrDefault("diagnosis", "").toString();
            String notes = request.getOrDefault("notes", "").toString();

            Optional<User> doctorOpt = userRepository.findById(doctorId);
            Optional<User> patientOpt = userRepository.findById(patientId);

            if (doctorOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Doctor not found with ID: " + doctorId));
            }
            if (patientOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Patient not found with ID: " + patientId));
            }

            Prescription prescription = new Prescription();
            prescription.setDoctor(doctorOpt.get());
            prescription.setPatient(patientOpt.get());
            prescription.setDiagnosis(diagnosis);
            prescription.setNotes(notes);

            // Safe appointment linking
            if (request.containsKey("appointmentId") && request.get("appointmentId") != null) {
                try {
                    Object apptObj = request.get("appointmentId");
                    if (apptObj != null && !apptObj.toString().isEmpty()) {
                        Long appointmentId = Long.valueOf(apptObj.toString());
                        Optional<Appointment> apptOpt = appointmentRepository.findById(appointmentId);
                        apptOpt.ifPresent(appt -> {
                            prescription.setAppointment(appt);
                            appt.setStatus(Appointment.Status.COMPLETED);
                            appointmentRepository.save(appt);
                        });
                    }
                } catch (Exception ignored) {
                    // Appointment linking failed, continue without it
                }
            }

            Prescription saved = prescriptionRepository.save(prescription);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", saved.getId());
            response.put("doctorName", saved.getDoctor().getFullName());
            response.put("patientName", saved.getPatient().getFullName());
            response.put("diagnosis", saved.getDiagnosis());
            response.put("notes", saved.getNotes());
            response.put("issuedDate", saved.getIssuedDate().toString());
            response.put("message", "Prescription created successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getPatientPrescriptions(Long patientId) {
        Optional<User> patientOpt = userRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Patient not found"));
        }

        List<Prescription> prescriptions = prescriptionRepository.findByPatient(patientOpt.get());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Prescription p : prescriptions) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("doctorName", p.getDoctor().getFullName());
            map.put("diagnosis", p.getDiagnosis());
            map.put("notes", p.getNotes());
            map.put("issuedDate", p.getIssuedDate().toString());
            map.put("status", p.getStatus().toString());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getDoctorPrescriptions(Long doctorId) {
        Optional<User> doctorOpt = userRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Doctor not found"));
        }

        List<Prescription> prescriptions = prescriptionRepository.findByDoctor(doctorOpt.get());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Prescription p : prescriptions) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("patientName", p.getPatient().getFullName());
            map.put("diagnosis", p.getDiagnosis());
            map.put("notes", p.getNotes());
            map.put("issuedDate", p.getIssuedDate().toString());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}