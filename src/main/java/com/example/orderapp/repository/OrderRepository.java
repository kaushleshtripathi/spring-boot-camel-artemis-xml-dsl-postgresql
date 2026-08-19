package com.example.orderapp.repository;

import com.example.orderapp.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByOrderNumber(String orderNumber);
}
