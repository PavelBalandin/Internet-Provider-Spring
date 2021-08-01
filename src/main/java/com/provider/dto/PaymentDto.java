package com.provider.dto;

import com.provider.entity.User;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDto {

    private Long id;

    @NotNull
    @DecimalMin(value = "10.00")
    @DecimalMax(value = "10000.00")
    @Digits(integer = 5, fraction = 2)
    private BigDecimal payment;

    private User user;

    private LocalDateTime created;
}
