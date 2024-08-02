package com.finalproject.airport.location.repository;

import com.finalproject.airport.location.entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Integer> {

    List<ZoneEntity> findAllByRegion(String region);

    List<ZoneEntity> findAllByRegionAndFloor(String region, String floor);

    @Query("SELECT z FROM ZoneEntity z WHERE z.region = :region AND z.floor = :floor AND z.location = :location")
    List<ZoneEntity> findByRegionAndFloorAndLocation(@Param("region") String region, @Param("floor") String floor, @Param("location") String location);

}
