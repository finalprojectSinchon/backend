package com.finalproject.airport.location.repository;

import com.finalproject.airport.location.entity.LocationEntity;
import com.finalproject.airport.location.entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {


    Boolean existsByFacilitiesCode(Integer zoneCode);


    LocationEntity findByFacilitiesCode(int code);

    Boolean existsByStorageCode(Integer airportCode);

    LocationEntity findByStorageCode(Integer airportCode);

    @Query("SELECT l FROM LocationEntity l WHERE l.storageCode IS NOT NULL")
    List<LocationEntity> findAllWithStorageCode();

    Boolean existsByBaggageClaimCode(Integer airportCode);

    LocationEntity findByBaggageClaimCode(Integer airportCode);
}
