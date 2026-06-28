package com.pharmacy.controller;

import com.pharmacy.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

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
}