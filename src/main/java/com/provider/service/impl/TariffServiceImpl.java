package com.provider.service.impl;

import com.provider.dto.TariffDto;
import com.provider.entity.*;
import com.provider.exception.NotEnoughFundsException;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
import com.provider.mapper.TariffMapper;
import com.provider.repository.PaymentRepository;
import com.provider.repository.TariffRepository;
import com.provider.repository.TariffUserRepository;
import com.provider.repository.UserRepository;
import com.provider.service.TariffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final TariffUserRepository tariffUserRepository;

    private final TariffMapper tariffMapper;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository, UserRepository userRepository, PaymentRepository paymentRepository, TariffUserRepository tariffUserRepository, TariffMapper tariffMapper) {
        this.tariffRepository = tariffRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.tariffUserRepository = tariffUserRepository;
        this.tariffMapper = tariffMapper;
    }

    @Override
    public Page<TariffDto> getAll(int page, int size, String sort, String order) {
        Page<Tariff> tariffPage = tariffRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort)));
        return tariffPage.map(tariffMapper::toDto);
    }

    @Override
    public List<TariffDto> getAll() {
        List<Tariff> tariffList = tariffRepository.findAll();
        return tariffMapper.toDtoList(tariffList);
    }

    @Override
    public TariffDto findById(Long id) {
        Tariff tariff = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return tariffMapper.toDto(tariff);
    }

    @Override
    public TariffDto create(TariffDto tariffDto) {
        Tariff tariff = tariffRepository.save(tariffMapper.toEntity(tariffDto));
        return tariffMapper.toDto(tariff);
    }

    @Override
    public TariffDto update(TariffDto tariffDto, Long id) {
        Tariff tariffFromDb = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        BeanUtils.copyProperties(tariffMapper.toEntity(tariffDto), tariffFromDb, "id");
        Tariff tariff = tariffRepository.save(tariffFromDb);
        return tariffMapper.toDto(tariff);
    }

    @Override
    public void delete(Long id) {
        Tariff tariff = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        tariffRepository.delete(tariff);
    }

    @Override
    public List<TariffDto> getTariffListByServiceId(Long id) {
        List<Tariff> tariffList = tariffRepository.findByServiceId(id);
        return tariffMapper.toDtoList(tariffList);
    }

    @Override
    public List<TariffDto> getTariffListByUserId(Long id) {
        List<Tariff> tariffList = tariffRepository.findByUserId(id);
        return tariffMapper.toDtoList(tariffList);
    }

    @Transactional
    public BigDecimal makeOrder(Long id, List<TariffDto> tariffDtoList) {
        List<Tariff> tariffList = tariffMapper.toEntityList(tariffDtoList);
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        BigDecimal tariffSum = tariffRepository.findTariffsSum(tariffList.stream().map(Tariff::getId).collect(Collectors.toList()));
        BigDecimal userSum = paymentRepository.getTotalUserSum(id);
        List<Tariff> userTariffs = tariffRepository.findByUserId(user.getId());
        if (userSum.compareTo(tariffSum) >= 0) {
            if (Collections.disjoint(
                    userTariffs.stream().map(Tariff::getId).collect(Collectors.toList()),
                    tariffList.stream().map(Tariff::getId).collect(Collectors.toList()))) {
                tariffList.forEach(tariff -> {
                    TariffUser tariffUser = new TariffUser();
                    tariffUser.setUser(user);
                    tariffUser.setTariff(tariff);
                    tariffUser.setDateStart(LocalDateTime.now());
                    tariffUser.setDateEnd(LocalDateTime.now().plusDays(tariff.getDuration()));
                    tariffUserRepository.save(tariffUser);
                });

                Payment payment = new Payment();
                payment.setUser(user);
                payment.setPayment(tariffSum.negate());
                paymentRepository.save(payment);
            } else {
                throw new ResourcesAlreadyExistsException();
            }
        } else {
            throw new NotEnoughFundsException();
        }
        return userSum.subtract(tariffSum);
    }

}