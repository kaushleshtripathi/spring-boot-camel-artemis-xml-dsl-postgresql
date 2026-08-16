package com.example.orderapp.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.LocalDateTime;
@Entity @Table(name="orders",uniqueConstraints=@UniqueConstraint(name="uk_order_number",columnNames="order_number"))
public class OrderEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(name="order_number",nullable=false,unique=true) private String orderNumber; @Column(name="customer_name",nullable=false) private String customerName; @Column(nullable=false,precision=14,scale=2) private BigDecimal amount; @Column(nullable=false) private String status; @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
public void setOrderNumber(String v){orderNumber=v;} public void setCustomerName(String v){customerName=v;} public void setAmount(BigDecimal v){amount=v;} public void setStatus(String v){status=v;} public void setCreatedAt(LocalDateTime v){createdAt=v;} }
