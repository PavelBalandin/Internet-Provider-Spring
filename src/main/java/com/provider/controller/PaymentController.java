package com.provider.controller;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;
import com.provider.entity.User;
import com.provider.mapper.PaymentMapper;
import com.provider.security.JwtProvider;
import com.provider.service.PaymentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    private final JwtProvider jwtProvider;

    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentController(PaymentService paymentService, JwtProvider jwtProvider, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.jwtProvider = jwtProvider;
        this.paymentMapper = paymentMapper;
    }

    @GetMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<List<PaymentDto>> getByUserId(@RequestHeader(name = "Authorization") String token) {
        log.trace("Getting payment list by user id");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(paymentMapper.listEntityToDTOList(paymentService.getByUserId(id)), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403")})
    public ResponseEntity<PaymentDto> create(@Valid @RequestBody PaymentDto paymentDTO, @RequestHeader(name = "Authorization") String token) {
        log.trace("Creating payment");
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setUser(User.builder().id(jwtProvider.getUserIdFromToken(token)).build());
        return new ResponseEntity<>(paymentMapper.toDto(paymentService.create(payment)), HttpStatus.CREATED);
    }
}
