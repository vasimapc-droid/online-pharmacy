package com.pharmacy.repository;

import com.pharmacy.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByRequiresPrescription(Boolean requiresPrescription);
    List<Medicine> findByNameContainingIgnoreCase(String keyword);
    List<Medicine> findByPharmacyId(Long pharmacyId);
}