package com.provider.service.impl;

import com.provider.entity.Payment;
import com.provider.repository.PaymentRepository;
import com.provider.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getAll() {
        return null;
    }

    @Override
    public Payment findById(Long id) {
        return null;
    }

    @Override
    public Payment findByName(String name) {
        return null;
    }

    @Override
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment update(Payment entity, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Payment> getByUserId(Long id) {
        return paymentRepository.findByUserId(id);
    }
}
