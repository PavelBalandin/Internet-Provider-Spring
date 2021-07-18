package com.provider;

import com.provider.entity.Payment;
import com.provider.repository.PaymentRepository;
import com.provider.service.PaymentService;
import com.provider.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        subject = new PaymentServiceImpl(paymentRepository);
    }

    @Test
    void getByUserId() {
        List<Payment> paymentsExpected = new ArrayList<>();
        when(paymentRepository.findByUserId(1L)).thenReturn(paymentsExpected);

        List<Payment> paymentsActual = subject.getByUserId(1L);

        assertEquals(paymentsExpected, paymentsActual);
    }

    @Test
    void save() {
        Payment paymentExpected = new Payment();
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentExpected);

        Payment paymentActual = subject.create(paymentExpected);

        assertEquals(paymentExpected, paymentActual);
    }
}
