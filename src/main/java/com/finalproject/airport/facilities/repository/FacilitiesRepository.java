package com.finalproject.airport.facilities.repository;

import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FacilitiesRepository  extends JpaRepository<FacilitiesEntity, Integer> {
    List<FacilitiesEntity> findAllByIsActive(String y);

    @Query("SELECT location FROM facilities")
    List<String> findAlllocations();


    @Query("SELECT location FROM facilities WHERE location = :location ")
    Integer findbylocation(String location);


}
