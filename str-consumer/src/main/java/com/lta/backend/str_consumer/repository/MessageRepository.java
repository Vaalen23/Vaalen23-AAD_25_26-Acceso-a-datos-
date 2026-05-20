package com.lta.backend.str_consumer.repository;


import com.lta.backend.str_consumer.model.KafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<KafkaMessage, Long> {
}
