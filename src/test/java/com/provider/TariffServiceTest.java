package com.provider;

import com.provider.entity.*;
import com.provider.exception.NotEnoughFundsException;
import com.provider.exception.ResourceNotFoundException;
import com.provider.exception.ResourcesAlreadyExistsException;
import com.provider.repository.PaymentRepository;
import com.provider.repository.TariffRepository;
import com.provider.repository.TariffUserRepository;
import com.provider.repository.UserRepository;
import com.provider.service.TariffService;
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
    UserRepository userRepository;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    TariffUserRepository tariffUserRepository;

    TariffService subject;

    @BeforeEach
    void setUp() {
        subject = new TariffServiceImpl(tariffRepository, userRepository, paymentRepository, tariffUserRepository);
    }

    @Test
    void findAll() {
        List<Tariff> tariffsExpected = new ArrayList<>();
        when(tariffRepository.findAll()).thenReturn(tariffsExpected);

        List<Tariff> tariffsActual = subject.getAll();

        assertEquals(tariffsExpected, tariffsActual);
    }

    @Test
    void findById() {
        Tariff tariffExpected = Tariff.builder().id(1L).build();
        when(tariffRepository.findById(1L)).thenReturn(Optional.of(tariffExpected));

        Tariff tariffActual = subject.findById(1L);

        assertEquals(tariffExpected, tariffActual);
    }

    @Test
    void shouldThrowExceptionWhenTariffNotFoundById() {
        when(tariffRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subject.findById(1L));
    }

    @Test
    void save() {
        Tariff tariffExpected = new Tariff();
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariffExpected);

        Tariff tariffActual = subject.create(tariffExpected);

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

        when(tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariffUpdated);

        Tariff tariffActual = subject.update(tariffUpdated, 1L);

        assertEquals(tariffUpdated, tariffActual);
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
        List<Tariff> orderTariffList = new ArrayList<>();
        orderTariffList.add(Tariff.builder().id(1L).build());
        orderTariffList.add(Tariff.builder().id(2L).build());
        orderTariffList.add(Tariff.builder().id(3L).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(tariffRepository.findTariffsSum(any())).thenReturn(BigDecimal.valueOf(400));
        when(paymentRepository.getTotalUserSum(1L)).thenReturn(BigDecimal.valueOf(500));
        when(tariffRepository.findByUserId(1L)).thenReturn(userTariffList);

        assertEquals(BigDecimal.valueOf(100), subject.makeOrder(1L, orderTariffList));

        verify(tariffUserRepository, times(3)).save(any(TariffUser.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotHaveEnoughFounds() {
        List<Tariff> userTariffList = new ArrayList<>();
        List<Tariff> orderTariffList = new ArrayList<>();
        orderTariffList.add(Tariff.builder().id(1L).build());
        orderTariffList.add(Tariff.builder().id(2L).build());
        orderTariffList.add(Tariff.builder().id(3L).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(tariffRepository.findTariffsSum(any())).thenReturn(BigDecimal.valueOf(600));
        when(paymentRepository.getTotalUserSum(1L)).thenReturn(BigDecimal.valueOf(500));
        when(tariffRepository.findByUserId(1L)).thenReturn(userTariffList);

        assertThrows(NotEnoughFundsException.class, () -> subject.makeOrder(1L, orderTariffList));
    }

    @Test
    void shouldThrowExceptionWhenUserHaveTariffsFromOrder() {
        List<Tariff> userTariffList = new ArrayList<>();
        userTariffList.add(Tariff.builder().id(1L).build());
        List<Tariff> orderTariffList = new ArrayList<>();
        orderTariffList.add(Tariff.builder().id(1L).build());
        orderTariffList.add(Tariff.builder().id(2L).build());
        orderTariffList.add(Tariff.builder().id(3L).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(tariffRepository.findTariffsSum(any())).thenReturn(BigDecimal.valueOf(400));
        when(paymentRepository.getTotalUserSum(1L)).thenReturn(BigDecimal.valueOf(500));
        when(tariffRepository.findByUserId(1L)).thenReturn(userTariffList);

        assertThrows(ResourcesAlreadyExistsException.class, () -> subject.makeOrder(1L, orderTariffList));
    }
}
