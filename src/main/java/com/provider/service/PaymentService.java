package com.provider.service;

import com.provider.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment create(Payment payment);

    List<Payment> getByUserId(Long id);
}
