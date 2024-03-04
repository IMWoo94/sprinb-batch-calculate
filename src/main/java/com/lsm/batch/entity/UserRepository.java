package com.lsm.batch.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lsm.batch.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
