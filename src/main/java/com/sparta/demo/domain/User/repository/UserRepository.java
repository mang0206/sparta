package com.sparta.demo.domain.User.repository;

import com.sparta.demo.domain.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}