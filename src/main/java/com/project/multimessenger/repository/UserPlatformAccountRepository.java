package com.project.multimessenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.multimessenger.model.UserPlatformAccount;

public interface UserPlatformAccountRepository extends JpaRepository<UserPlatformAccount, Long> {
    Optional<UserPlatformAccount> findByUserIdAndPlatformId(Long userId, Long platformId);
}