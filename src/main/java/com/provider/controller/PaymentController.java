package com.provider.controller;

import com.provider.entity.Payment;
import com.provider.security.JwtProvider;
import com.provider.service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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
        log.trace("Getting payment list by user id");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(paymentService.getByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment, @RequestHeader(name = "Authorization") String token) {
        log.trace("Creating payment");
        payment.getUser().setId(jwtProvider.getUserIdFromToken(token));
        return new ResponseEntity<>(paymentService.create(payment), HttpStatus.CREATED);
    }
}
