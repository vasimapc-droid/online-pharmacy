package com.pharmacy.repository;

import com.pharmacy.model.Prescription;
import com.pharmacy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(User patient);
    List<Prescription> findByDoctor(User doctor);
    List<Prescription> findByPatientAndStatus(User patient, Prescription.Status status);
}