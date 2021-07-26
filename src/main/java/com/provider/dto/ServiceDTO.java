package com.provider.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ServiceDTO {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;
}
