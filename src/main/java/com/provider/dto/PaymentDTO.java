package com.provider.dto;

import com.provider.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private Long id;

    private BigDecimal payment;

    private User user;

    private LocalDateTime created;
}
