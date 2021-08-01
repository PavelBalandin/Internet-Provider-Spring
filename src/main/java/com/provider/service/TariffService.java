package com.provider.service;

import com.provider.dto.TariffDto;
import com.provider.entity.Tariff;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface TariffService {
    List<TariffDto> getAll();

    TariffDto findById(Long id);

    TariffDto create(TariffDto entity);

    TariffDto update(TariffDto entity, Long id);

    void delete(Long id);

    Page<TariffDto> getAll(int page, int size, String sort, String order);

    List<TariffDto> getTariffListByServiceId(Long id);

    List<TariffDto> getTariffListByUserId(Long id);

    BigDecimal makeOrder(Long id, List<TariffDto> tariffList);
}
