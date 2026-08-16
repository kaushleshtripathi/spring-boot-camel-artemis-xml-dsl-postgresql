package com.example.orderapp.api;
import com.example.orderapp.model.OrderRequest;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequestMapping("/api/orders") public class OrderController {
 private final ProducerTemplate producer; public OrderController(ProducerTemplate producer){this.producer=producer;}
 @PostMapping public ResponseEntity<String> create(@RequestHeader(value="Idempotency-Key",required=false) String key,@RequestBody OrderRequest order){
  String id=(key==null||key.isBlank())?UUID.randomUUID().toString():key;
  producer.sendBodyAndHeader("jms:queue:orders.in",order,"Idempotency-Key",id);
  return ResponseEntity.accepted().body("Accepted. Idempotency-Key="+id);
 }
}
