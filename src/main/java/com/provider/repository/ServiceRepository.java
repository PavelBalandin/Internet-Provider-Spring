package com.provider.repository;

import com.provider.entity.Service;
import com.provider.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    Service findByTariffListIn(List<Tariff> tariffList);
}
