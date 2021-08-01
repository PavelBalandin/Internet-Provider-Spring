package com.provider.service.impl;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;
import com.provider.mapper.PaymentMapper;
import com.provider.repository.PaymentRepository;
import com.provider.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentDto create(PaymentDto paymentDto) {
        Payment payment = paymentRepository.save(paymentMapper.toEntity(paymentDto));
        return paymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentDto> getByUserId(Long id) {
        List<Payment> paymentList = paymentRepository.findByUserId(id);
        return paymentMapper.toDtoList(paymentList);
    }
}
