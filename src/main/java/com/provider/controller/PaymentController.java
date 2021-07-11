package com.provider.controller;

import com.provider.entity.Payment;
import com.provider.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Payment>> getByUserId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        return new ResponseEntity<>(paymentService.getByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        return new ResponseEntity<>(paymentService.create(payment), HttpStatus.CREATED);
    }
}
