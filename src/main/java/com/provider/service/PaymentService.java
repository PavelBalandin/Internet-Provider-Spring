package com.provider.service;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PaymentDto createFromDto(PaymentDto paymentDto);

    Payment create(Payment payment);

    List<PaymentDto> getByUserId(Long id);

    BigDecimal getTotalUserSum(Long id);
}
