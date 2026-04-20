package com.project.multimessenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.multimessenger.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}