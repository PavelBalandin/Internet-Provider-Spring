package com.provider.service;

import com.provider.entity.Tariff;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TariffService extends BaseService<Tariff> {

    Page<Tariff> getAll(int page, int size, String sort, String order);

    List<Tariff> getTariffListByServiceId(Long id);
}
