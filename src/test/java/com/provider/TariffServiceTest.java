package com.provider;

import com.provider.entity.Service;
import com.provider.entity.Tariff;
import com.provider.exception.ResourceNotFoundException;
import com.provider.service.TariffService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(value = {"/create-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TariffServiceTest {

    @Autowired
    private TariffService tariffService;

    @Test
    public void shouldReturnTariffList() {
        assertFalse(tariffService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnTariffPage() {
        Page<Tariff> page = tariffService.getAll(1, 5, "price", "ASC");
        assertEquals(11, page.getTotalElements());
        assertEquals(5, page.getSize());
        assertEquals(1, page.getNumber());
        assertEquals(new BigDecimal("200.00"), page.getContent().get(0).getPrice());
    }

    @Test
    public void getTariffsByUserId() {
        List<Tariff> tariffList = tariffService.getTariffListByUserId(2L);
        assertFalse(tariffList.isEmpty());
    }

    @Test
    public void getTariffsByServiceId() {
        List<Tariff> tariffList = tariffService.getTariffListByServiceId(1L);
        assertFalse(tariffList.isEmpty());
    }

    @Test
    public void createTariff() {
        Tariff tariff = tariffService.create(Tariff
                .builder()
                .name("Test tariff")
                .description("Test tariff description")
                .duration(30)
                .price(new BigDecimal("999.00"))
                .service(Service.builder().id(1L).build())
                .build());
        assertEquals(tariff.getName(), "Test tariff");
        assertEquals(tariff.getDescription(), "Test tariff description");
        assertEquals(tariff.getDuration(), 30);
        assertEquals(tariff.getPrice(), new BigDecimal("999.00"));
        assertEquals(tariff.getService().getId(), 1L);
    }

    @Test
    public void updateTariff() {
        Tariff updatedTariff = tariffService.create(Tariff
                .builder()
                .name("Updated name")
                .description("Updated description")
                .duration(90)
                .price(new BigDecimal("888.00"))
                .service(Service.builder().id(2L).build())
                .build());
        Tariff tariff = tariffService.update(updatedTariff, 3L);
        assertEquals(3L, tariff.getId());
        assertEquals("Updated name", tariff.getName());
        assertEquals("Updated description", tariff.getDescription());
        assertEquals(90, tariff.getDuration());
        assertEquals(new BigDecimal("888.00"), tariff.getPrice());
        assertEquals(2L, tariff.getService().getId());
    }

    @Test
    public void deleteTariff() {
        tariffService.delete(5L);
        assertThrows(ResourceNotFoundException.class, () -> {
            tariffService.findById(5L);
        });
    }
}
