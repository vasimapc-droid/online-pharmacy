package com.pharmacy.repository;

import com.pharmacy.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrderBySentAtAsc(Long senderId, Long receiverId);
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
    List<Message> findByAppointmentIdOrderBySentAtAsc(Long appointmentId);
}