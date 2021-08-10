package com.provider.service;

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
import com.provider.service.impl.TariffServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TariffServiceTest {

    @Mock
    TariffRepository tariffRepository;
    @Mock
    UserService userService;
    @Mock
    PaymentService paymentService;
    @Mock
    TariffUserRepository tariffUserRepository;

    TariffService subject;

    @BeforeEach
    void setUp() {
        subject = new TariffServiceImpl(tariffRepository, tariffUserRepository, userService, paymentService, new TariffMapper());
    }

    @Test
    void findAll() {
        List<TariffDto> tariffsExpected = new ArrayList<>();
        tariffsExpected.add(TariffDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(30)
                .price(BigDecimal.valueOf(100))
                .service(Service.builder().id(1L).build())
                .build());

        List<Tariff> tariffList = new ArrayList<>();
        tariffList.add(Tariff.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(30)
                .price(BigDecimal.valueOf(100))
                .service(Service.builder().id(1L).build())
                .build());

        when(tariffRepository.findAll()).thenReturn(tariffList);

        List<TariffDto> tariffsActual = subject.getAll();

        assertEquals(tariffsExpected, tariffsActual);
    }

    @Test
    void findById() {
        TariffDto tariffExpected = new TariffDto();
        tariffExpected.setId(1L);

        Tariff tariff = Tariff.builder().id(1L).build();
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        TariffDto tariffActual = subject.findById(1L);

        assertEquals(tariffExpected, tariffActual);
    }

    @Test
    void shouldThrowExceptionWhenTariffNotFoundById() {
        when(tariffRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subject.findById(1L));
    }

    @Test
    void save() {
        TariffDto tariffExpected = new TariffDto();

        Tariff tariff = new Tariff();
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariff);

        TariffDto tariffActual = subject.create(tariffExpected);

        assertEquals(tariffActual, tariffExpected);
    }

    @Test
    void update() {
        Tariff tariff = Tariff.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(30)
                .price(BigDecimal.valueOf(100))
                .service(Service.builder().id(1L).build())
                .build();

        Tariff tariffUpdated = Tariff.builder()
                .id(1L)
                .name("nameUpdated")
                .description("descriptionUpdated")
                .duration(60)
                .price(BigDecimal.valueOf(200))
                .service(Service.builder().id(2L).build())
                .build();

        TariffDto tariffDto = TariffDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(30)
                .price(BigDecimal.valueOf(100))
                .service(Service.builder().id(1L).build())
                .build();

        TariffDto tariffExpected = TariffDto.builder()
                .id(1L)
                .name("nameUpdated")
                .description("descriptionUpdated")
                .duration(60)
                .price(BigDecimal.valueOf(200))
                .service(Service.builder().id(2L).build())
                .build();

        when(tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariffUpdated);

        TariffDto tariffActual = subject.update(tariffDto, 1L);

        assertEquals(tariffExpected, tariffActual);
    }

    @Test
    void delete() {
        Tariff tariff = Tariff.builder().id(1L).build();
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        subject.delete(1L);

        verify(tariffRepository, times(1)).delete(tariff);
    }

    @Test
    void makeOrder() {
        List<Tariff> userTariffList = new ArrayList<>();
        List<TariffDto> orderTariffList = new ArrayList<>();
        orderTariffList.add(TariffDto.builder().id(1L).build());
        orderTariffList.add(TariffDto.builder().id(2L).build());
        orderTariffList.add(TariffDto.builder().id(3L).build());

        when(userService.findUserById(1L)).thenReturn(User.builder().id(1L).build());
        when(tariffRepository.findTariffsSum(any())).thenReturn(BigDecimal.valueOf(400));
        when(paymentService.getTotalUserSum(1L)).thenReturn(BigDecimal.valueOf(500));
        when(tariffRepository.findByUserId(1L)).thenReturn(userTariffList);

        assertEquals(BigDecimal.valueOf(100), subject.makeOrder(1L, orderTariffList));

        verify(tariffUserRepository, times(3)).save(any(TariffUser.class));
        verify(paymentService, times(1)).create(any(Payment.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotHaveEnoughFounds() {
        List<Tariff> userTariffList = new ArrayList<>();
        List<TariffDto> orderTariffList = new ArrayList<>();
        orderTariffList.add(TariffDto.builder().id(1L).build());
        orderTariffList.add(TariffDto.builder().id(2L).build());
        orderTariffList.add(TariffDto.builder().id(3L).build());

        when(userService.findUserById(1L)).thenReturn(User.builder().id(1L).build());
        when(tariffRepository.findTariffsSum(any())).thenReturn(BigDecimal.valueOf(600));
        when(paymentService.getTotalUserSum(1L)).thenReturn(BigDecimal.valueOf(500));
        when(tariffRepository.findByUserId(1L)).thenReturn(userTariffList);

        assertThrows(NotEnoughFundsException.class, () -> subject.makeOrder(1L, orderTariffList));
    }

    @Test
    void shouldThrowExceptionWhenUserHaveTariffsFromOrder() {
        List<Tariff> userTariffList = new ArrayList<>();
        userTariffList.add(Tariff.builder().id(1L).build());
        List<TariffDto> orderTariffList = new ArrayList<>();
        orderTariffList.add(TariffDto.builder().id(1L).build());
        orderTariffList.add(TariffDto.builder().id(2L).build());
        orderTariffList.add(TariffDto.builder().id(3L).build());

        when(userService.findUserById(1L)).thenReturn(User.builder().id(1L).build());
        when(tariffRepository.findTariffsSum(any())).thenReturn(BigDecimal.valueOf(400));
        when(paymentService.getTotalUserSum(1L)).thenReturn(BigDecimal.valueOf(500));
        when(tariffRepository.findByUserId(1L)).thenReturn(userTariffList);

        assertThrows(ResourcesAlreadyExistsException.class, () -> subject.makeOrder(1L, orderTariffList));
    }
}
