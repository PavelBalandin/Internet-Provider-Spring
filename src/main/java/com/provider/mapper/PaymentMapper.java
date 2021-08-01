package com.provider.mapper;

import com.provider.dto.PaymentDto;
import com.provider.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMapper {
    public PaymentDto toDto(Payment payment) {
        PaymentDto paymentDTO = new PaymentDto();
        paymentDTO.setId(payment.getId());
        paymentDTO.setPayment(payment.getPayment());
        paymentDTO.setUser(payment.getUser());
        paymentDTO.setCreated(payment.getCreated());

        return paymentDTO;
    }

    public Payment toEntity(PaymentDto paymentdto) {
        Payment payment = new Payment();
        payment.setId(paymentdto.getId());
        payment.setPayment(paymentdto.getPayment());
        payment.setUser(paymentdto.getUser());
        payment.setCreated(paymentdto.getCreated());

        return payment;
    }

    public List<PaymentDto> listEntityToDTOList(List<Payment> paymentList) {
        return paymentList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
