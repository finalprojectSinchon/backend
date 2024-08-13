package com.finalproject.airport.storage.repository;

import com.finalproject.airport.storage.entity.StorageEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StorageRepository extends JpaRepository<StorageEntity, Integer> {


    List<StorageEntity> findByisActive(String y);


    StorageEntity findBystorageCode(Integer storageCode);


    StorageEntity findByLocation(String location);

    @Query("SELECT cc.status AS status, COUNT(cc) AS count FROM storage cc GROUP BY cc.status")
    List<Object[]> findStorageStatusCounts();

    @Query("SELECT location FROM storage ")
    List<String> findAlllocations();
}
