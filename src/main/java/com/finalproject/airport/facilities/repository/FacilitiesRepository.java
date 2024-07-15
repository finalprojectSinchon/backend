package com.finalproject.airport.facilities.repository;

import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface FacilitiesRepository  extends JpaRepository<FacilitiesEntity, Integer> {
}
