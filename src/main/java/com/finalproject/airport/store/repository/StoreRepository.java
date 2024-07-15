package com.finalproject.airport.store.repository;

import com.finalproject.airport.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
}
