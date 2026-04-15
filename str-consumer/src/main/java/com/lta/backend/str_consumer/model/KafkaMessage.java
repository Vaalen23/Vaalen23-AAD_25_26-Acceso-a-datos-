package com.lta.backend.str_consumer.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
public class KafkaMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    public KafkaMessage(String content) {
        this.content = content;
        this.receivedAt = LocalDateTime.now();
    }
}