package com.pharmacy.repository;

import com.pharmacy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPatientId(Long patientId);
    List<Order> findByPharmacyId(Long pharmacyId);
    List<Order> findByStatus(Order.Status status);
}