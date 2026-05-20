package com.lta.backend.str_consumer.service;

import com.lta.backend.str_consumer.model.KafkaMessage;
import com.lta.backend.str_consumer.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public void saveMessage(String content) {
        KafkaMessage savedMessage = messageRepository.save(new KafkaMessage(content));
        log.info("Mensaje almacenado en BD con ID: {}", savedMessage.getId());
    }
}