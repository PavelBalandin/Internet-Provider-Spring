package com.provider.service;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;

import java.util.List;

public interface PaymentService {
    PaymentDto create(PaymentDto paymentDto);

    List<PaymentDto> getByUserId(Long id);
}
