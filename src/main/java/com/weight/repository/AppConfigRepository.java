// src/main/java/com/weight/repository/AdminLogRepository.java
package com.weight.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weight.model.AppConfig;

public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
    
	Optional<AppConfig> findByParamKey(String paramKey);
}