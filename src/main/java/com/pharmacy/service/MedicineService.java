package com.pharmacy.service;

import com.pharmacy.model.Medicine;
import com.pharmacy.model.User;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Map<String, Object>> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Medicine m : medicines) {
            result.add(medicineToMap(m));
        }
        return result;
    }

    public List<Map<String, Object>> getOTCMedicines() {
        List<Medicine> medicines = medicineRepository.findByRequiresPrescription(false);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Medicine m : medicines) {
            result.add(medicineToMap(m));
        }
        return result;
    }

    public List<Map<String, Object>> searchMedicines(String keyword) {
        List<Medicine> medicines = medicineRepository.findByNameContainingIgnoreCase(keyword);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Medicine m : medicines) {
            result.add(medicineToMap(m));
        }
        return result;
    }

    public ResponseEntity<?> addMedicine(Map<String, Object> request) {
        try {
            Long pharmacyId = Long.valueOf(request.get("pharmacyId").toString());
            Optional<User> pharmacyOpt = userRepository.findById(pharmacyId);

            if (pharmacyOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Pharmacy not found"));
            }

            Medicine medicine = new Medicine();
            medicine.setName(request.get("name").toString());
            medicine.setDescription(request.getOrDefault("description", "").toString());
            medicine.setPrice(Double.valueOf(request.get("price").toString()));
            medicine.setStockQuantity(Integer.valueOf(request.getOrDefault("stockQuantity", "0").toString()));
            medicine.setRequiresPrescription(Boolean.valueOf(request.getOrDefault("requiresPrescription", "false").toString()));
            medicine.setPharmacy(pharmacyOpt.get());

            Medicine saved = medicineRepository.save(medicine);
            return ResponseEntity.ok(medicineToMap(saved));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error adding medicine: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> updateStock(Long id, Map<String, Object> request) {
        Optional<Medicine> medOpt = medicineRepository.findById(id);
        if (medOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Medicine not found"));
        }

        Medicine medicine = medOpt.get();
        medicine.setStockQuantity(Integer.valueOf(request.get("stockQuantity").toString()));
        medicineRepository.save(medicine);

        return ResponseEntity.ok(Map.of("message", "Stock updated", "newStock", medicine.getStockQuantity()));
    }

    private Map<String, Object> medicineToMap(Medicine m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", m.getId());
        map.put("name", m.getName());
        map.put("description", m.getDescription());
        map.put("price", m.getPrice());
        map.put("stockQuantity", m.getStockQuantity());
        map.put("requiresPrescription", m.getRequiresPrescription());
        map.put("type", m.getRequiresPrescription() ? "Rx" : "OTC");
        map.put("pharmacyName", m.getPharmacy() != null ? m.getPharmacy().getPharmacyName() : "N/A");
        return map;
    }
}
