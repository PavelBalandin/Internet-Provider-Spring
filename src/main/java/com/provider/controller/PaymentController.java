package com.provider.controller;

import com.provider.entity.Payment;
import com.provider.security.JwtProvider;
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
    private final JwtProvider jwtProvider;

    @Autowired
    public PaymentController(PaymentService paymentService, JwtProvider jwtProvider) {
        this.paymentService = paymentService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/user")
    public ResponseEntity<List<Payment>> getByUserId(@RequestHeader(name = "Authorization") String token) {
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(paymentService.getByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment, @RequestHeader(name = "Authorization") String token) {
        payment.getUser().setId(jwtProvider.getUserIdFromToken(token));
        return new ResponseEntity<>(paymentService.create(payment), HttpStatus.CREATED);
    }
}
