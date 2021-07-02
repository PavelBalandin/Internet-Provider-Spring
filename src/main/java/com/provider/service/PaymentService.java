package com.provider.service;

import com.provider.entity.Payment;

import java.util.List;

public interface PaymentService extends BaseService<Payment> {

    List<Payment> getByUserId(Long id);
}
