package com.provider;

import com.provider.entity.Payment;
import com.provider.entity.User;
import com.provider.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Sql(value = {"/create-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void shouldReturnPaymentListByUserId() {
        assertFalse(paymentService.getByUserId(3L).isEmpty());
    }

    @Test
    void createPayment() {
        Payment payment = paymentService.create(Payment
                .builder()
                .payment(new BigDecimal("700.00"))
                .user(User.builder().id(3L).build())
                .build());
        assertEquals(new BigDecimal("700.00"), payment.getPayment());
        assertEquals(3L, payment.getUser().getId());
    }

}
