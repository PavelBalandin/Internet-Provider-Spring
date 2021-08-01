package com.provider.dto;

import com.provider.entity.Service;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class TariffDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @Size(min = 10, max = 255)
    private String description;

    @NotNull
    @Min(value = 1)
    @Max(value = 365)
    private int duration;

    @NotNull
    @DecimalMin(value = "10.00")
    @DecimalMax(value = "10000.00")
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Service service;
}
