package com.provider.repository;

import com.provider.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long id);

    @Query("SELECT sum(p.payment) from Payment p where p.user.id = :userId")
    BigDecimal getTotalUserSum(@Param("userId") Long id);
}
