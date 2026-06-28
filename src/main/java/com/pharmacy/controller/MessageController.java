package com.pharmacy.controller;

import com.pharmacy.model.Appointment;
import com.pharmacy.model.Message;
import com.pharmacy.model.User;
import com.pharmacy.repository.AppointmentRepository;
import com.pharmacy.repository.MessageRepository;
import com.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            Long senderId = Long.valueOf(request.get("senderId").toString());
            Long receiverId = Long.valueOf(request.get("receiverId").toString());
            String content = request.get("content").toString();

            Optional<User> senderOpt = userRepository.findById(senderId);
            Optional<User> receiverOpt = userRepository.findById(receiverId);

            if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Sender or receiver not found"));
            }

            Message message = new Message();
            message.setSender(senderOpt.get());
            message.setReceiver(receiverOpt.get());
            message.setContent(content);

            if (request.containsKey("appointmentId") && request.get("appointmentId") != null) {
                Long appointmentId = Long.valueOf(request.get("appointmentId").toString());
                Optional<Appointment> apptOpt = appointmentRepository.findById(appointmentId);
                apptOpt.ifPresent(message::setAppointment);
            }

            Message saved = messageRepository.save(message);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("id", saved.getId());
            response.put("senderId", saved.getSender().getId());
            response.put("senderName", saved.getSender().getFullName());
            response.put("receiverId", saved.getReceiver().getId());
            response.put("content", saved.getContent());
            response.put("sentAt", saved.getSentAt().toString());
            response.put("message", "Message sent successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(@RequestParam Long user1, @RequestParam Long user2) {
        List<Message> messages1 = messageRepository.findBySenderIdAndReceiverIdOrderBySentAtAsc(user1, user2);
        List<Message> messages2 = messageRepository.findBySenderIdAndReceiverIdOrderBySentAtAsc(user2, user1);

        List<Map<String, Object>> allMessages = new ArrayList<>();

        for (Message m : messages1) {
            allMessages.add(messageToMap(m));
        }
        for (Message m : messages2) {
            allMessages.add(messageToMap(m));
        }

        allMessages.sort((a, b) -> a.get("sentAt").toString().compareTo(b.get("sentAt").toString()));

        return ResponseEntity.ok(allMessages);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getMessagesByAppointment(@PathVariable Long appointmentId) {
        List<Message> messages = messageRepository.findByAppointmentIdOrderBySentAtAsc(appointmentId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Message m : messages) {
            result.add(messageToMap(m));
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        Optional<Message> msgOpt = messageRepository.findById(id);
        if (msgOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message not found"));
        }
        Message message = msgOpt.get();
        message.setIsRead(true);
        messageRepository.save(message);
        return ResponseEntity.ok(Map.of("message", "Marked as read"));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> getUnreadCount(@PathVariable Long userId) {
        List<Message> unread = messageRepository.findByReceiverIdAndIsReadFalse(userId);
        return ResponseEntity.ok(Map.of("unreadCount", unread.size()));
    }

    private Map<String, Object> messageToMap(Message m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", m.getId());
        map.put("senderId", m.getSender().getId());
        map.put("senderName", m.getSender().getFullName());
        map.put("receiverId", m.getReceiver().getId());
        map.put("content", m.getContent());
        map.put("sentAt", m.getSentAt().toString());
        map.put("isRead", m.getIsRead());
        if (m.getAppointment() != null) {
            map.put("appointmentId", m.getAppointment().getId());
        }
        return map;
    }
}