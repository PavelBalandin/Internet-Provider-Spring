package com.provider.repository;

import com.provider.entity.TariffUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffUserRepository extends JpaRepository<TariffUser, Long> {
}
