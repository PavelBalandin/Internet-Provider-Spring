package com.provider.service;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;
import com.provider.entity.User;
import com.provider.mapper.PaymentMapper;
import com.provider.repository.PaymentRepository;
import com.provider.service.PaymentService;
import com.provider.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    PaymentService subject;

    @BeforeEach
    void setUp() {
        subject = new PaymentServiceImpl(paymentRepository, new PaymentMapper());
    }

    @Test
    void getByUserId() {
        User user = new User();
        List<PaymentDto> paymentsExpected = new ArrayList<>();
        paymentsExpected.add(PaymentDto.builder().id(1L).payment(BigDecimal.valueOf(100)).user(user).build());

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(Payment.builder().id(1L).payment(BigDecimal.valueOf(100)).user(user).build());

        when(paymentRepository.findByUserId(1L)).thenReturn(paymentList);

        List<PaymentDto> paymentsActual = subject.getByUserId(1L);

        assertEquals(paymentsExpected, paymentsActual);
    }

    @Test
    void save() {
        User user = new User();
        PaymentDto paymentExpected = PaymentDto.builder().id(1L).payment(BigDecimal.valueOf(100)).user(user).build();

        Payment payment = Payment.builder().id(1L).payment(BigDecimal.valueOf(100)).user(user).build();
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDto paymentActual = subject.create(paymentExpected);

        assertEquals(paymentExpected, paymentActual);
    }
}
