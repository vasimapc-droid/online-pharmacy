package com.pharmacy.controller;

import com.pharmacy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientOrders(@PathVariable Long patientId) {
        return orderService.getPatientOrders(patientId);
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<?> getPharmacyOrders(@PathVariable Long pharmacyId) {
        return orderService.getPharmacyOrders(pharmacyId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return orderService.updateStatus(id, request.get("status"));
    }
}