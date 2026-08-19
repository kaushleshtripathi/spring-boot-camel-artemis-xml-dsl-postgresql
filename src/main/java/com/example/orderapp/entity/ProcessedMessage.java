package com.example.orderapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_messages")
public class ProcessedMessage {
    @Id
    @Column(name = "message_key", length = 200)
    private String messageKey;
    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    protected ProcessedMessage() {
    }

    public ProcessedMessage(String k) {
        messageKey = k;
        processedAt = LocalDateTime.now();
    }
}
