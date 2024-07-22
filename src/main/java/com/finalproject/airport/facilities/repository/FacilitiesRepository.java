package com.finalproject.airport.facilities.repository;

import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FacilitiesRepository  extends JpaRepository<FacilitiesEntity, Integer> {
    List<FacilitiesEntity> findAllByIsActive(String y);


}
