package com.example.orderapp.model;
import java.math.BigDecimal;
public class OrderRequest { private String orderNumber; private String customerName; private BigDecimal amount;
public String getOrderNumber(){return orderNumber;} public void setOrderNumber(String v){orderNumber=v;}
public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;} }
