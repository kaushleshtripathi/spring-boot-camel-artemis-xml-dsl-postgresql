package com.example.orderapp.service;

import com.example.orderapp.entity.*;
import com.example.orderapp.model.OrderRequest;
import com.example.orderapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("orderTransactionService")
public class OrderTransactionService {
    private final OrderRepository orders;
    private final ProcessedMessageRepository messages;

    public OrderTransactionService(OrderRepository o, ProcessedMessageRepository m) {
        orders = o;
        messages = m;
    }

    @Transactional
    public void process(String key, OrderRequest request) {
        if (messages.existsById(key)) {
            throw new DuplicateMessageException("Duplicate idempotency key: " + key);
        }
        System.out.println("key :"+key);
        if (request == null) {
            throw new IllegalArgumentException("Request map cannot be null: "+ request);
        }
        if (orders.existsByOrderNumber(request.getOrderNumber())) {
            throw new DuplicateMessageException("Duplicate order: " + request.getOrderNumber());
        }
        if ("FAIL".equalsIgnoreCase(request.getCustomerName())) {
            throw new SimulatedFailureException("Simulated processing failure");
        }


        OrderEntity e = new OrderEntity();
        e.setOrderNumber(request.getOrderNumber());
        e.setCustomerName(request.getCustomerName());
        e.setAmount(request.getAmount());
        e.setStatus("PROCESSED");
        e.setCreatedAt(LocalDateTime.now());
        orders.save(e);
        messages.save(new ProcessedMessage(key));
    }

    public static class DuplicateMessageException extends RuntimeException {
        public DuplicateMessageException(String m) {
            super(m);
        }
    }

    public static class SimulatedFailureException extends RuntimeException {
        public SimulatedFailureException(String m) {
            super(m);
        }
    }
}
