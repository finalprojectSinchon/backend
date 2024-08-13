package com.finalproject.airport.store.repository;

import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {

    List<StoreEntity> findByIsActive(String y);


    StoreEntity findBystoreId(Integer storeId);


    StoreEntity findBystoreLocation(String location);

    @Query("SELECT cc.status AS status, COUNT(cc) AS count FROM store cc GROUP BY cc.status")
    List<Object[]> findStoreStatusCounts();

    @Query("SELECT s.storeLocation FROM store s ")
    List<String> findAlllocations();


}
