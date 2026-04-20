package com.project.multimessenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.multimessenger.model.Platform;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByPlatformNameIgnoreCase(String platformName);
}