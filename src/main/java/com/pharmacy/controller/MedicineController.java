package com.pharmacy.controller;

import com.pharmacy.model.Medicine;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping
    public List<Map<String, Object>> getAllMedicines() {
        return medicineService.getAllMedicines();
    }

    @GetMapping("/otc")
    public List<Map<String, Object>> getOTCMedicines() {
        return medicineService.getOTCMedicines();
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchMedicines(@RequestParam String keyword) {
        return medicineService.searchMedicines(keyword);
    }

    @PostMapping
    public ResponseEntity<?> addMedicine(@RequestBody Map<String, Object> request) {
        return medicineService.addMedicine(request);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        return medicineService.updateStock(id, request);
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<?> getMedicinesByPharmacy(@PathVariable Long pharmacyId) {
        List<Medicine> medicines = medicineRepository.findByPharmacyId(pharmacyId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Medicine m : medicines) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", m.getId());
            map.put("name", m.getName());
            map.put("description", m.getDescription());
            map.put("price", m.getPrice());
            map.put("stockQuantity", m.getStockQuantity());
            map.put("requiresPrescription", m.getRequiresPrescription());
            map.put("pharmacyName", m.getPharmacy() != null ? m.getPharmacy().getPharmacyName() : "N/A");
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }
}