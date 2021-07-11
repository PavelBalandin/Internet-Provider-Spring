package com.provider.repository;

import com.provider.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    List<Tariff> findByServiceId(Long id);

    @Query(value = "SELECT t.*, tu.date_start, tu.date_end,\n" +
            "s.id AS serviceid, s.name AS servicename  FROM users u\n" +
            "JOIN tariff_user tu ON u.id = tu.user_id\n" +
            "JOIN tariffs t ON t.id = tu.tariff_id\n" +
            "JOIN services s ON s.id = t.service_id\n" +
            "where u.id = ?;", nativeQuery = true)
    List<Tariff> findByUserId(Long id);

    @Query("SELECT sum(t.price) from Tariff t where t.id in :ids")
    BigDecimal findTariffsSum(@Param("ids") List<Long> tariffListId);
}
