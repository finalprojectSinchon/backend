package com.finalproject.airport.storage.repository;

import com.finalproject.airport.storage.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageRepository extends JpaRepository<StorageEntity, Integer> {


    List<StorageEntity> findByisActive(String y);

    StorageEntity findBystorageCode(Integer storageCode);
}
