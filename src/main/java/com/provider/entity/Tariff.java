package com.provider.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tariffs")
@Embeddable
public class Tariff extends BaseEntity {
    private String name;

    private String description;

    private int duration;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @JsonIgnore
    @OneToMany(mappedBy = "tariff")
    private List<TariffUser> tariffUserList;
}
