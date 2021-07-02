package com.provider.service.impl;

import com.provider.entity.Tariff;
import com.provider.exception.ResourceNotFoundException;
import com.provider.repository.TariffRepository;
import com.provider.service.TariffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Page<Tariff> getAll(int page, int size, String sort, String order) {
        return tariffRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort)));
    }

    @Override
    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }

    @Override
    public Tariff findById(Long id) {
        return tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Tariff findByName(String name) {
        return null;
    }

    @Override
    public Tariff create(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    @Override
    public Tariff update(Tariff tariff, Long id) {
        Tariff tariffFromDb = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        BeanUtils.copyProperties(tariff, tariffFromDb, "id");
        return tariffRepository.save(tariffFromDb);
    }

    @Override
    public void delete(Long id) {
        Tariff tariff = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        tariffRepository.delete(tariff);
    }

    @Override
    public List<Tariff> getTariffListByServiceId(Long id) {
        return tariffRepository.findByServiceId(id);
    }
}
