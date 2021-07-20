package com.provider.dto;

import com.provider.entity.Service;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TariffDTO {

    private Long id;

    private String name;

    private String description;

    private int duration;

    private BigDecimal price;

    private Service service;
}
