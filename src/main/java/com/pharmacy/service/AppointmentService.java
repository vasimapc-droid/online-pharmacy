package com.pharmacy.service;

import com.pharmacy.model.Appointment;
import com.pharmacy.model.User;
import com.pharmacy.repository.AppointmentRepository;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> bookAppointment(Map<String, Object> request) {
        try {
            Long patientId = Long.valueOf(request.get("patientId").toString());
            Long doctorId = Long.valueOf(request.get("doctorId").toString());
            String dateStr = request.get("appointmentDate").toString();
            String reason = request.getOrDefault("reason", "").toString();

            Optional<User> patientOpt = userRepository.findById(patientId);
            Optional<User> doctorOpt = userRepository.findById(doctorId);

            if (patientOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Patient not found with ID: " + patientId));
            }
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Doctor not found with ID: " + doctorId));
            }

            Appointment appointment = new Appointment();
            appointment.setPatient(patientOpt.get());
            appointment.setDoctor(doctorOpt.get());
            appointment.setAppointmentDate(LocalDateTime.parse(dateStr));
            appointment.setReason(reason);
            appointment.setStatus(Appointment.Status.BOOKED);

            Appointment saved = appointmentRepository.save(appointment);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", saved.getId());
            response.put("patient", saved.getPatient().getFullName());
            response.put("doctor", saved.getDoctor().getFullName());
            response.put("appointmentDate", saved.getAppointmentDate().toString());
            response.put("reason", saved.getReason());
            response.put("status", saved.getStatus().toString());
            response.put("message", "Appointment booked successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getPatientAppointments(Long patientId) {
        Optional<User> patientOpt = userRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Patient not found"));
        }

        List<Appointment> appointments = appointmentRepository.findByPatient(patientOpt.get());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment a : appointments) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", a.getId());
            map.put("doctorName", a.getDoctor().getFullName());
            map.put("appointmentDate", a.getAppointmentDate().toString());
            map.put("reason", a.getReason());
            map.put("status", a.getStatus().toString());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getDoctorAppointments(Long doctorId) {
        Optional<User> doctorOpt = userRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Doctor not found"));
        }

        List<Appointment> appointments = appointmentRepository.findByDoctor(doctorOpt.get());
        List<Map<String, Object>> result = new ArrayList<>();

        for (Appointment a : appointments) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", a.getId());
            map.put("patientId", a.getPatient().getId());
            map.put("patientName", a.getPatient().getFullName());
            map.put("appointmentDate", a.getAppointmentDate().toString());
            map.put("reason", a.getReason());
            map.put("status", a.getStatus().toString());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> updateStatus(Long id, String status) {
        Optional<Appointment> apptOpt = appointmentRepository.findById(id);
        if (apptOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Appointment not found"));
        }

        Appointment appointment = apptOpt.get();
        try {
            appointment.setStatus(Appointment.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status. Use BOOKED, COMPLETED, or CANCELLED"));
        }
        appointmentRepository.save(appointment);

        return ResponseEntity.ok(Map.of("message", "Appointment status updated to " + appointment.getStatus()));
    }
}