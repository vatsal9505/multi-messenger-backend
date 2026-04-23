package com.project.multimessenger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.multimessenger.model.UserPlatformAccount;

public interface UserPlatformAccountRepository
        extends JpaRepository<UserPlatformAccount, Long> {

    List<UserPlatformAccount> findByUserId(Long userId);

    Optional<UserPlatformAccount> findByUserIdAndPlatformId(
            Long userId,
            Long platformId
    );
}