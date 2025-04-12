package com.skillconnect.server.controller;

import com.skillconnect.server.model.AdminMessage;
import com.skillconnect.server.service.AdminMessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-messages")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminMessageController {

    private final AdminMessageService adminMessageService;

    @PostMapping
    public ResponseEntity<AdminMessage> createMessage(@RequestBody AdminMessage message) {
        AdminMessage created = adminMessageService.createMessage(message);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminMessage> getMessageById(@PathVariable int id) {
        AdminMessage message = new AdminMessage();
        message.setMessageId(id);

        return adminMessageService.findById(message)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AdminMessage>> getAllMessages() {
        return ResponseEntity.ok(adminMessageService.findAllMessages());
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AdminMessage>> getMessagesByAdminId(@PathVariable int adminId) {
        AdminMessage admin = new AdminMessage();
        admin.getAdmin().setUserId(adminId);

        return ResponseEntity.ok(adminMessageService.findMessagesByAdminId(admin));
    }

    @PutMapping
    public ResponseEntity<AdminMessage> updateMessage(@RequestBody AdminMessage adminMessage) {
        return ResponseEntity.ok(adminMessageService.updateMessage(adminMessage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        adminMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}

