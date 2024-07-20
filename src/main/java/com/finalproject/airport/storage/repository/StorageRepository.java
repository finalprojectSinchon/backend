package com.finalproject.airport.storage.repository;

import com.finalproject.airport.storage.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<StorageEntity, Integer> {
}
