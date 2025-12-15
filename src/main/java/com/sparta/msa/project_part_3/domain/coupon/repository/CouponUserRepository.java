package com.sparta.msa.project_part_3.domain.coupon.repository;

import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CouponUser c WHERE c.code = :code")
    Optional<CouponUser> findByCodeForUpdate(@Param("code") String code);

    Optional<CouponUser> findByCode(String code);

    List<CouponUser> findByUserId(Long userId);
}