package com.example.orderapp.repository;
import com.example.orderapp.entity.ProcessedMessage; import org.springframework.data.jpa.repository.JpaRepository;
public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage,String>{}
