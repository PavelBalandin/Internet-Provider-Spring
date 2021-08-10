package com.provider.mapper;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PaymentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PaymentDto toDto(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

    public Payment toEntity(PaymentDto paymentDto) {
        return modelMapper.map(paymentDto, Payment.class);
    }

    public List<PaymentDto> toDtoList(List<Payment> paymentList) {
        return paymentList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
