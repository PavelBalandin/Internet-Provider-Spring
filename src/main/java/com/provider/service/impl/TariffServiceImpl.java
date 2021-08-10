package com.provider.service.impl;

import com.provider.dto.TariffDto;
import com.provider.entity.*;
import com.provider.exception.NotEnoughFundsException;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
import com.provider.mapper.TariffMapper;
import com.provider.repository.TariffRepository;
import com.provider.repository.TariffUserRepository;
import com.provider.service.PaymentService;
import com.provider.service.TariffService;
import com.provider.service.UserService;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    private final TariffUserRepository tariffUserRepository;

    private final UserService userService;

    private final PaymentService paymentService;

    private final TariffMapper tariffMapper;

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository, TariffUserRepository tariffUserRepository, UserService userService, PaymentService paymentService, TariffMapper tariffMapper) {
        this.tariffRepository = tariffRepository;
        this.tariffUserRepository = tariffUserRepository;
        this.userService = userService;
        this.paymentService = paymentService;
        this.tariffMapper = tariffMapper;
    }

    @Override
    public Page<TariffDto> getAll(int page, int size, String sort, String order) {
        Page<Tariff> tariffPage = tariffRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort)));
        log.trace("Tariff page list has been fetched");
        return tariffPage.map(tariffMapper::toDto);
    }

    @Override
    public List<TariffDto> getAll() {
        List<Tariff> tariffList = tariffRepository.findAll();
        log.trace("Tariff list has been fetched");
        return tariffMapper.toDtoList(tariffList);
    }

    @Override
    public TariffDto findById(Long id) {
        Tariff tariff = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        log.trace("Tariff with id: " + id + " has been fetched");
        return tariffMapper.toDto(tariff);
    }

    @Override
    public TariffDto create(TariffDto tariffDto) {
        Tariff tariff = tariffRepository.save(tariffMapper.toEntity(tariffDto));
        log.info("Tariff has been created:" + tariff.getName());
        return tariffMapper.toDto(tariff);
    }

    @Override
    public TariffDto update(TariffDto tariffDto, Long id) {
        Tariff tariffFromDb = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        BeanUtils.copyProperties(tariffMapper.toEntity(tariffDto), tariffFromDb, "id");
        Tariff tariff = tariffRepository.save(tariffFromDb);
        log.info("Tariff has been updated:" + tariff.getName());
        return tariffMapper.toDto(tariff);
    }

    @Override
    public void delete(Long id) {
        Tariff tariff = tariffRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        tariffRepository.delete(tariff);
        log.info("Tariff has been deleted:" + tariff.getName());
    }

    @Override
    public List<TariffDto> getTariffListByServiceId(Long id) {
        List<Tariff> tariffList = tariffRepository.findByServiceId(id);
        log.trace("Tariff list has been fetched by service id: " + id);
        return tariffMapper.toDtoList(tariffList);
    }

    @Override
    public List<TariffDto> getTariffListByUserId(Long id) {
        List<Tariff> tariffList = tariffRepository.findByUserId(id);
        log.trace("Tariff list has been fetched by user id: " + id);
        return tariffMapper.toDtoList(tariffList);
    }

    @Transactional
    public BigDecimal makeOrder(Long id, List<TariffDto> tariffDtoList) {
        List<Tariff> tariffList = tariffMapper.toEntityList(tariffDtoList);
        User user = userService.findUserById(id);
        BigDecimal tariffSum = tariffRepository.findTariffsSum(tariffList.stream().map(Tariff::getId).collect(Collectors.toList()));
        BigDecimal userSum = paymentService.getTotalUserSum(id);
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
                paymentService.create(payment);
            } else {
                throw new ResourcesAlreadyExistsException();
            }
        } else {
            throw new NotEnoughFundsException();
        }
        return userSum.subtract(tariffSum);
    }

}