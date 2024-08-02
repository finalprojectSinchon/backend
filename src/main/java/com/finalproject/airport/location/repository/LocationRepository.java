package com.finalproject.airport.location.repository;

import com.finalproject.airport.location.entity.LocationEntity;
import com.finalproject.airport.location.entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {


    Boolean existsByFacilitiesCode(Integer zoneCode);


    LocationEntity findByFacilitiesCode(int code);
}
