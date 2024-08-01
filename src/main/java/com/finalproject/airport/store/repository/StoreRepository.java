package com.finalproject.airport.store.repository;

import com.finalproject.airport.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {

    List<StoreEntity> findByIsActive(String y);


//    Store findByLocation(String location);
}
