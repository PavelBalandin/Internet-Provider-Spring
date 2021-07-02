package com.provider.repository;

import com.provider.entity.Tariff;
import com.provider.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    List<Tariff> findByServiceId(Long id);

    List<Tariff> findByTariffUserListIn(List<User> userList);
}
