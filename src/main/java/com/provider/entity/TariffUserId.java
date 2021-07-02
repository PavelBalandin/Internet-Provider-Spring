package com.provider.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TariffUserId implements Serializable {
    private Long tariff;

    private Long user;
}
