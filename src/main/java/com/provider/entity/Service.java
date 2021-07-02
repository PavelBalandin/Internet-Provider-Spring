package com.provider.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "services")
public class Service extends BaseEntity {
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<Tariff> tariffList;
}
