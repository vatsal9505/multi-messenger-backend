package com.project.multimessenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.multimessenger.model.Platform;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    Optional<Platform> findByPlatformNameIgnoreCase(String platformName);

}