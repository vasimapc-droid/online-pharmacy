package com.pharmacy.service;

import com.pharmacy.model.Medicine;
import com.pharmacy.model.Order;
import com.pharmacy.model.Prescription;
import com.pharmacy.model.User;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.OrderRepository;
import com.pharmacy.repository.PrescriptionRepository;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public ResponseEntity<?> placeOrder(Map<String, Object> request) {
        try {
            Long patientId = Long.valueOf(request.get("patientId").toString());
            Long medicineId = Long.valueOf(request.get("medicineId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            String deliveryAddress = request.getOrDefault("deliveryAddress", "").toString();

            // Validate address
            if (deliveryAddress == null || deliveryAddress.trim().length() < 5) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Please provide a valid delivery address"));
            }

            Optional<User> patientOpt = userRepository.findById(patientId);
            Optional<Medicine> medicineOpt = medicineRepository.findById(medicineId);

            if (patientOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Patient not found"));
            }
            if (medicineOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Medicine not found"));
            }

            Medicine medicine = medicineOpt.get();

            // ========== Rx MEDICINE: Check prescription ==========
            if (medicine.getRequiresPrescription() != null && medicine.getRequiresPrescription()) {

                List<Prescription> prescriptions = prescriptionRepository
                        .findByPatientAndStatus(patientOpt.get(), Prescription.Status.ACTIVE);

                System.out.println("=== DEBUG Rx ORDER ===");
                System.out.println("Patient: " + patientId);
                System.out.println("Medicine: " + medicine.getName());
                System.out.println("Active prescriptions found: " + prescriptions.size());

                boolean hasValidPrescription = false;
                Prescription matchedPrescription = null;

                for (Prescription p : prescriptions) {
                    System.out.println("Checking Rx #" + p.getId() + ": " + p.getNotes());
                    if (p.getNotes() != null &&
                            p.getNotes().toLowerCase().contains(medicine.getName().toLowerCase())) {
                        hasValidPrescription = true;
                        matchedPrescription = p;
                        System.out.println("MATCHED! Rx #" + p.getId());
                        break;
                    }
                }

                if (!hasValidPrescription) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error",
                                    "This medicine requires a valid prescription. Please consult a doctor first."));
                }

                // Check stock
                if (medicine.getStockQuantity() < quantity) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Insufficient stock. Available: " + medicine.getStockQuantity()));
                }

                // Create order
                Order order = new Order();
                order.setPatient(patientOpt.get());
                order.setMedicine(medicine);
                order.setPharmacy(medicine.getPharmacy());
                order.setQuantity(quantity);
                order.setTotalPrice(medicine.getPrice() * quantity);
                order.setDeliveryAddress(deliveryAddress.trim());

                // Reduce stock
                medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
                medicineRepository.save(medicine);

                Order saved = orderRepository.save(order);

                // MARK PRESCRIPTION AS FULFILLED
                if (matchedPrescription != null) {
                    matchedPrescription.setStatus(Prescription.Status.FULFILLED);
                    prescriptionRepository.save(matchedPrescription);
                    System.out.println("Rx #" + matchedPrescription.getId() + " → FULFILLED");
                }

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("id", saved.getId());
                response.put("patientName", saved.getPatient().getFullName());
                response.put("medicineName", saved.getMedicine().getName());
                response.put("quantity", saved.getQuantity());
                response.put("totalPrice", saved.getTotalPrice());
                response.put("deliveryAddress", saved.getDeliveryAddress());
                response.put("status", saved.getStatus().toString());
                response.put("requiresPrescription", true);
                response.put("prescriptionFulfilled", true);
                response.put("orderDate", saved.getOrderDate().toString());
                response.put("message", "Order placed successfully. Prescription marked as fulfilled.");

                return ResponseEntity.ok(response);

            }
            // ========== OTC MEDICINE: No prescription needed ==========
            else {

                // Check stock
                if (medicine.getStockQuantity() < quantity) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Insufficient stock. Available: " + medicine.getStockQuantity()));
                }

                // Create order
                Order order = new Order();
                order.setPatient(patientOpt.get());
                order.setMedicine(medicine);
                order.setPharmacy(medicine.getPharmacy());
                order.setQuantity(quantity);
                order.setTotalPrice(medicine.getPrice() * quantity);
                order.setDeliveryAddress(deliveryAddress.trim());

                // Reduce stock
                medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
                medicineRepository.save(medicine);

                Order saved = orderRepository.save(order);

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("id", saved.getId());
                response.put("patientName", saved.getPatient().getFullName());
                response.put("medicineName", saved.getMedicine().getName());
                response.put("quantity", saved.getQuantity());
                response.put("totalPrice", saved.getTotalPrice());
                response.put("deliveryAddress", saved.getDeliveryAddress());
                response.put("status", saved.getStatus().toString());
                response.put("requiresPrescription", false);
                response.put("orderDate", saved.getOrderDate().toString());
                response.put("message", "Order placed successfully.");

                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error placing order: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> getPatientOrders(Long patientId) {
        List<Order> orders = orderRepository.findByPatientId(patientId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Order o : orders) {
            result.add(orderToMap(o));
        }
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getPharmacyOrders(Long pharmacyId) {
        List<Order> orders = orderRepository.findByPharmacyId(pharmacyId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Order o : orders) {
            result.add(orderToMap(o));
        }
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> updateStatus(Long id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Order not found"));
        }

        Order order = orderOpt.get();
        try {
            order.setStatus(Order.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid status. Use PENDING, CONFIRMED, SHIPPED, DELIVERED, or CANCELLED"));
        }
        orderRepository.save(order);

        return ResponseEntity.ok(Map.of("message", "Order status updated to " + order.getStatus()));
    }

    private Map<String, Object> orderToMap(Order o) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", o.getId());
        map.put("patientName", o.getPatient().getFullName());
        map.put("patientPhone", o.getPatient().getPhone());
        map.put("medicineName", o.getMedicine().getName());
        map.put("medicineId", o.getMedicine().getId());
        map.put("quantity", o.getQuantity());
        map.put("totalPrice", o.getTotalPrice());
        map.put("deliveryAddress", o.getDeliveryAddress());
        map.put("status", o.getStatus().toString());
        map.put("orderDate", o.getOrderDate().toString());
        map.put("pharmacyName", o.getPharmacy() != null ? o.getPharmacy().getPharmacyName() : "N/A");
        map.put("requiresPrescription", o.getMedicine().getRequiresPrescription());
        return map;
    }
}