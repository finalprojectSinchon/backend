package com.finalproject.airport.facilities.repository;

import com.finalproject.airport.facilities.entity.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;



public interface FacilitiesRepository  extends JpaRepository<Facilities , Integer> {
}
