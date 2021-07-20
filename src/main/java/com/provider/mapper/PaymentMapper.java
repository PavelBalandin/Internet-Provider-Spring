package com.provider.mapper;

import com.provider.dto.PaymentDTO;
import com.provider.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMapper {
    public PaymentDTO entityToDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setPayment(payment.getPayment());
        paymentDTO.setUser(payment.getUser());
        paymentDTO.setCreated(payment.getCreated());

        return paymentDTO;
    }

    public Payment DTOtoEntity(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setPayment(paymentDTO.getPayment());
        payment.setUser(paymentDTO.getUser());
        payment.setCreated(paymentDTO.getCreated());

        return payment;
    }

    public List<PaymentDTO> listEntityToDTOList(List<Payment> paymentList) {
        return paymentList.stream().map(this::entityToDTO).collect(Collectors.toList());
    }
}
