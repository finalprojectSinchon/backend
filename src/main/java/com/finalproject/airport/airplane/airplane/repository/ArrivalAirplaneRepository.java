package com.finalproject.airport.airplane.airplane.repository;

import com.finalproject.airport.airplane.airplane.Entity.ArrivalAirplane;
import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArrivalAirplaneRepository extends JpaRepository<ArrivalAirplane , Integer> {
    DepartureAirplane findByAirplaneCode(int airplaneCode);

    List<ArrivalAirplane> findByCarousel(int i);
}
