package com.provider.service.impl;

import com.provider.entity.*;
import com.provider.exception.NotEnoughFundsException;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
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

    @Autowired
    public TariffServiceImpl(TariffRepository tariffRepository, UserRepository userRepository, PaymentRepository paymentRepository, TariffUserRepository tariffUserRepository) {
        this.tariffRepository = tariffRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.tariffUserRepository = tariffUserRepository;
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

    @Override
    public List<Tariff> getTariffListByUserId(Long id) {
        return tariffRepository.findByUserId(id);
    }

    @Transactional
    public BigDecimal makeOrder(Long id, List<Tariff> tariffList) {
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