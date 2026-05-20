package com.lta.backend.str_consumer.controller;

import com.lta.backend.str_consumer.model.KafkaMessage;
import com.lta.backend.str_consumer.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping
    public List<KafkaMessage> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/{id}")
    public KafkaMessage getMessageById(@PathVariable Long id) {
        return messageRepository.findById(id).orElse(null);
    }
}
